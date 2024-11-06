import { useMemo, useState } from "react";
import { ErrorScreen } from "../../../components/ErrorBoundary";
import LoadingScreen from "../../../components/LoadingScreen";
import {
  useDeleteActionLogsByUser,
  useGetActionLogs,
  useGetAllUsersAsAdmin,
} from "../../../hooks/api/adminApiHooks";
import { groupByLogAction } from "../../../utils/groupByUtils";
import Autocomplete from "@mui/material/Autocomplete";
import { TextField } from "@mui/material";
import { KeycloakUser } from "../../../types/types";
import DeleteModal from "../../../components/DeleteModal";
import StatisticChart from "../../../components/Admin/StatisticChart";
import { toast } from "react-toastify";

export const AdminStatisticsPage = () => {
  const { data: logEntries, isLoading, error, refetch } = useGetActionLogs();
  const {
    data: users,
    isLoading: isLoadingUsers,
    error: errorUsers,
  } = useGetAllUsersAsAdmin();

  const deleteActionLogsByUserMutation = useDeleteActionLogsByUser();

  const [selectedUser, setSelectedUser] = useState<KeycloakUser | null>(null);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);

  const filteredLogEntries = useMemo(() => {
    if (!selectedUser || !logEntries) return logEntries;
    return logEntries.filter((log) => log.userId === selectedUser.id);
  }, [selectedUser, logEntries]);

  const groupedData = useMemo(() => {
    if (filteredLogEntries) {
      return groupByLogAction(filteredLogEntries);
    }
    return null;
  }, [filteredLogEntries]);

  const handleDeleteLogs = () => {
    if (!selectedUser) return;
    deleteActionLogsByUserMutation.mutate(selectedUser.id.toString(), {
      onSuccess: () => {
        setIsDeleteDialogOpen(false);
        toast.success("Logs deleted successfully!");
        setSelectedUser(null);
        refetch();
      },
      onError: (error) => {
        toast.error(error.message);
      },
    });
  };

  console.log("logEntries", groupedData);

  return (
    <>
      {isLoading && isLoadingUsers && <LoadingScreen />}
      {error && errorUsers && <ErrorScreen message={error.message} />}
      {logEntries && users && (
        <div>
          <h1 className="text-2xl font-bold p-6">Action Logs Statistics</h1>
          <div className="bg-white card p-5">
            <Autocomplete
              value={selectedUser}
              disablePortal
              renderInput={(params) => (
                <TextField {...params} label="Search by username..." />
              )}
              options={users}
              getOptionLabel={(option) => option.username}
              onChange={(_, user) => setSelectedUser(user)}
            />
          </div>
          <StatisticChart groupedData={groupedData} />

          {selectedUser && filteredLogEntries && (
            <div className="mt-5 flex justify-between items-start">
              <div className="flex gap-5 flex-wrap flex-col">
                <h1 className="text-2xl font-bold">
                  Action Logs for {selectedUser.username}
                </h1>
                {filteredLogEntries.map((log) => (
                  <div key={log.id}>
                    {new Date(log.timestamp).toLocaleString()} - {log.action}
                  </div>
                ))}
              </div>
              <button
                className="btn btn-primary"
                onClick={() => setIsDeleteDialogOpen(true)}
              >
                Clear log history
              </button>

              {isDeleteDialogOpen && (
                <DeleteModal
                  text={"Are you sure you want to delete user logs?"}
                  onClose={() => setIsDeleteDialogOpen(false)}
                  onSubmit={handleDeleteLogs}
                />
              )}
            </div>
          )}
        </div>
      )}
    </>
  );
};
