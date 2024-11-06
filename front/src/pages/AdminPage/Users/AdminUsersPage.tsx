import { useState } from "react";
import Pagination from "../../../components/Pagination";
import { useGetUsersAsAdmin } from "../../../hooks/api/adminApiHooks";
import { ErrorScreen } from "../../../components/ErrorBoundary";
import LoadingScreen from "../../../components/LoadingScreen";
import UserBox from "../../../components/Admin/UserBox";
import { useDebounce } from "use-debounce";

const SIZE = 10;

const AdminUsersPage = () => {
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);
  const [debouncedSearch] = useDebounce(search, 1000);

  const { data, isLoading, error } = useGetUsersAsAdmin(
    page,
    SIZE,
    debouncedSearch
  );

  return (
    <>
      {isLoading && <LoadingScreen />}
      {error && <ErrorScreen message={error.message} />}
      {data && (
        <>
          <div className="flex gap-5 mt-5">
            <input
              value={search}
              type="text"
              className="input input-primary w-full"
              placeholder="Search users..."
              onChange={(e) => setSearch(e.target.value)}
            />
          </div>

          <div className="flex gap-5 flex-wrap p-10">
            {data.content.map((user) => (
              <UserBox user={user} key={user.id} />
            ))}
          </div>

          {data.content.length === 0 && (
            <div className="flex justify-center items-center h-96">
              <p className="text-lg font-semibold text-primary">
                No users found! 😢
              </p>
            </div>
          )}

          <Pagination
            page={page}
            setPage={(page) => setPage(page)}
            totalPage={data.totalPages}
          />
        </>
      )}
    </>
  );
};

export default AdminUsersPage;
