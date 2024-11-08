import { SubscriptionPlan } from "../../types/types";
import LoadingScreen from "../../components/LoadingScreen";
import {
  useChangeUserSubscriptionPlan,
  useDeleteUserAccount,
} from "../../hooks/api/userProfileApiHooks";
import { useEffect, useState } from "react";
import ChangePlanModal from "../../components/ChangePlanModal";
import { toast } from "react-toastify";
import PhotoBox from "../../components/PhotoBox";
import { ErrorScreen } from "../../components/ErrorBoundary";
import DeleteModal from "../../components/DeleteModal";
import { useGetPhotosByUserId } from "../../hooks/api/photoApiHooks";
import { useAuthContext } from "../../hooks/useAuthContext";
import { useUserDetailsContext } from "../../hooks/useUserDetailsContext";

const ProfilePage = () => {
  const { user, accountManagement } = useAuthContext();
  const { userDetails } = useUserDetailsContext();
  const { data: userPhotos, isLoading, error } = useGetPhotosByUserId(user!.id);

  const changeSubscriptionPlanMutation = useChangeUserSubscriptionPlan();
  const deleteUserAccountMutation = useDeleteUserAccount();

  const [
    isChangeSubscriptionPlanModalOpen,
    setIsChangeSubscriptionPlanModalOpen,
  ] = useState(false);

  const [isDeleteAccountModalOpen, setIsDeleteAccountModalOpen] =
    useState(false);

  const [isProfileCompleted, setIsProfileCompleted] = useState(false);

  const handleSubmitChangeSubscriptionPlan = async (
    newPlan: SubscriptionPlan
  ) => {
    changeSubscriptionPlanMutation.mutate(newPlan, {
      onSuccess: () => {
        setIsChangeSubscriptionPlanModalOpen(false);
        toast.success("Subscription plan changed successfully!");
      },
      onError: (error) => {
        toast.error(error.message);
      },
    });
  };

  const handleDeleteAccount = async () => {
    deleteUserAccountMutation.mutate(undefined, {
      onSuccess: () => {
        toast.success("Account deleted successfully!");
      },
      onError: (error) => {
        toast.error(error.message);
      },
    });
  };

  useEffect(() => {
    if (userDetails.isProfileCompleted) {
      setIsProfileCompleted(true);
    }
  }, [userDetails.isProfileCompleted]);

  return (
    <>
      {isLoading && <LoadingScreen />}
      {error && <ErrorScreen message={error.message} />}
      {user && userPhotos && (
        <div className="p-8 min-h-screen">
          <div className="max-w-6xl mx-auto">
            {/* User Info Card */}
            <div className="card bg-white shadow-xl p-6 mb-8 flex flex-row justify-between">
              <div className="flex items-center space-x-6">
                <div className="avatar">
                  <div className="w-24 rounded-full ring ring-primary ring-offset-base-100 ring-offset-2">
                    <img
                      src={`https://api.multiavatar.com/${user.username}.svg`}
                      alt="User avatar"
                    />
                  </div>
                </div>
                <div>
                  {isProfileCompleted ? (
                    <h1 className="text-2xl font-bold">
                      Name: {`${user.firstName} ${user.lastName}`}
                    </h1>
                  ) : (
                    <h1 className="text-2xl font-bold">
                      Please complete your profile
                    </h1>
                  )}
                  <p className="text-gray-500">Username: {user.username}</p>
                  {user.email && (
                    <p className="text-gray-500">Email: {user.email}</p>
                  )}
                  {isProfileCompleted && (
                    <p className="text-gray-500">
                      Role:{" "}
                      <span className="badge badge-accent">{user.role}</span>
                    </p>
                  )}
                  {isProfileCompleted && (
                    <p className="mt-2">
                      <span className="badge badge-primary">
                        Plan: {userDetails.subscriptionPlan}
                      </span>
                      <span className="badge badge-secondary ml-2">
                        Uploads today: {userDetails.dailyUpload}
                      </span>
                    </p>
                  )}
                </div>
              </div>
              <div className="flex flex-col justify-center">
                {isProfileCompleted && (
                  <>
                    <button
                      className="btn btn-primary"
                      onClick={() => setIsChangeSubscriptionPlanModalOpen(true)}
                    >
                      Change Plan
                    </button>
                    <button
                      className="btn btn-primary mt-2"
                      onClick={() => accountManagement()}
                    >
                      Account Settings
                    </button>
                  </>
                )}
                <button
                  className="btn btn-error mt-2"
                  onClick={() => setIsDeleteAccountModalOpen(true)}
                >
                  Delete Account
                </button>
              </div>
            </div>

            {/* User Pictures */}
            {isProfileCompleted && (
              <div className="card shadow-xl p-6">
                <h2 className="text-xl font-semibold mb-4">Your Pictures</h2>
                <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                  {userPhotos.map((picture, index) => (
                    <PhotoBox key={index} photo={picture} />
                  ))}
                  {userPhotos.length === 0 && (
                    <p className="text-gray-500">No pictures uploaded yet.</p>
                  )}
                </div>
              </div>
            )}
          </div>

          {/* Change Subscription Plan Modal */}
          {isChangeSubscriptionPlanModalOpen && (
            <ChangePlanModal
              currentSubscriptionPlan={userDetails.subscriptionPlan}
              onClose={() => setIsChangeSubscriptionPlanModalOpen(false)}
              onSubmit={handleSubmitChangeSubscriptionPlan}
            />
          )}

          {/* Delete Account Modal */}
          {isDeleteAccountModalOpen && (
            <DeleteModal
              onClose={() => setIsDeleteAccountModalOpen(false)}
              onSubmit={handleDeleteAccount}
              text={"Are you sure you want to delete your account?"}
            />
          )}
        </div>
      )}
    </>
  );
};

export default ProfilePage;
