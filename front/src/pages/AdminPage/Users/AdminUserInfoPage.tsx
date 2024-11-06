import { useNavigate, useParams } from "react-router-dom";
import {
  useDeleteUserAsAdmin,
  useGetUserByIdAsAdmin,
  useGetUserDetailsByIdAsAdmin,
  useUpdateUserAsAdmin,
} from "../../../hooks/api/adminApiHooks";
import { useEffect, useState } from "react";
import LoadingScreen from "../../../components/LoadingScreen";
import { ErrorScreen } from "../../../components/ErrorBoundary";
import ModifyProfileModal from "../../../components/Admin/ModifyProfileModal";
import { UpdateUserData } from "../../../types/types";
import { toast } from "react-toastify";
import DeleteModal from "../../../components/DeleteModal";

const AdminUserInfoPage = () => {
  const { id } = useParams<string>();
  const navigate = useNavigate();
  const {
    data: userInfo,
    isLoading,
    error,
    refetch: refetchUserInfo,
  } = useGetUserByIdAsAdmin(id!);
  const {
    data: userDetails,
    isLoading: userDetailsIsLoading,
    error: userDetailsError,
    refetch: refetchUserDetails,
  } = useGetUserDetailsByIdAsAdmin(id!);

  const updateUserAsAdminMutation = useUpdateUserAsAdmin();
  const deleteUserAsAdminMutation = useDeleteUserAsAdmin();

  const [isProfileCompleted, setIsProfileCompleted] = useState(false);
  const [isModifyModalOpen, setIsModifyModalOpen] = useState(false);
  const [isDeleteAccountModalOpen, setIsDeleteAccountModalOpen] =
    useState(false);

  useEffect(() => {
    if (userDetails) {
      setIsProfileCompleted(userDetails.isProfileCompleted);
    }
  }, [userDetails]);

  const handleSubmitModifyModal = (userData: UpdateUserData) => {
    const { id } = userInfo!;
    if (!id) {
      return;
    }
    updateUserAsAdminMutation.mutate(
      {
        userId: id.toString(),
        userData: userData,
      },
      {
        onSuccess: () => {
          setIsModifyModalOpen(false);
          refetchUserInfo();
          refetchUserDetails();
          toast.success("User profile updated successfully!");
        },
        onError: (error) => {
          setIsModifyModalOpen(false);
          toast.error(error.message);
        },
      }
    );
  };

  const handleSubmitDeleteAccountModal = () => {
    const { id } = userInfo!;
    if (!id) {
      return;
    }
    deleteUserAsAdminMutation.mutate(id.toString(), {
      onSuccess: () => {
        setIsDeleteAccountModalOpen(false);
        navigate("/admin/users");
        toast.success("User account deleted successfully!");
      },
      onError: (error) => {
        setIsDeleteAccountModalOpen(false);
        toast.error(error.message);
      },
    });
  };

  return (
    <div>
      {(isLoading || userDetailsIsLoading) && <LoadingScreen />}
      {(error || userDetailsError) && <ErrorScreen message={error?.message} />}
      {userInfo && userDetails && (
        <div className="card bg-white shadow-xl p-6 mb-8 flex flex-row justify-between">
          <div className="flex items-center space-x-6">
            <div className="avatar">
              <div className="w-24 rounded-full ring ring-primary ring-offset-base-100 ring-offset-2">
                <img
                  src={`https://api.multiavatar.com/${userInfo.username}.svg`}
                  alt="User avatar"
                />
              </div>
            </div>
            <div>
              <h1 className="text-2xl font-bold">
                Name: {`${userInfo.firstName} ${userInfo.lastName}`}
              </h1>

              <p className="text-gray-500">Username: {userInfo.username}</p>

              <p className="text-gray-500">
                <span>
                  Plan:{" "}
                  {isProfileCompleted
                    ? userDetails.subscriptionPlan
                    : "Profile not completed"}
                </span>
              </p>
              <p className="text-gray-500">
                <span>Uploads today: {userDetails.dailyUpload}</span>
              </p>
            </div>
          </div>
          <div className="flex flex-col justify-center">
            <button
              className="btn btn-primary"
              onClick={() => setIsModifyModalOpen(true)}
            >
              Modify Profile
            </button>
            <button
              className="btn btn-error mt-2"
              onClick={() => setIsDeleteAccountModalOpen(true)}
            >
              Delete Account
            </button>
          </div>
        </div>
      )}
      {isModifyModalOpen && userInfo && userDetails && (
        <ModifyProfileModal
          user={{
            firstName: userInfo.firstName,
            lastName: userInfo.lastName,
            role: userInfo.role,
            subscriptionPlan: userDetails.subscriptionPlan,
          }}
          onClose={() => setIsModifyModalOpen(false)}
          onSubmit={handleSubmitModifyModal}
        />
      )}

      {isDeleteAccountModalOpen && userInfo && (
        <DeleteModal
          onClose={() => setIsDeleteAccountModalOpen(false)}
          onSubmit={handleSubmitDeleteAccountModal}
          text={"Are you sure you want to delete this user account?"}
        />
      )}
    </div>
  );
};

export default AdminUserInfoPage;
