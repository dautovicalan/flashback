import { createContext, useEffect, useState } from "react";
import { SubscriptionPlan, UserDetails } from "../types/types";
import { useKeycloak } from "@react-keycloak/web";
import { getUserProfile } from "../pages/ProfilePage/api/profileApi";

type UserDetailsContextType = {
  userDetails: UserDetails;
  setUserDetails: (userDetails: UserDetails) => void;
  completeProfile: (userData: UserDetails) => void;
  increaseDailyUpload: () => void;
};

export const UserDetailsContext = createContext<
  UserDetailsContextType | undefined
>(undefined);

type UserDetailsProviderProps = {
  children: React.ReactNode;
};

export const UserDetailsProvider: React.FC<UserDetailsProviderProps> = ({
  children,
}) => {
  const { keycloak } = useKeycloak();

  const [userDetails, setUserDetails] = useState<UserDetails>({
    subscriptionPlan: SubscriptionPlan.FREE,
    isProfileCompleted: false,
    dailyUpload: 0,
  });

  useEffect(() => {
    const getUserDetils = async () => {
      if (keycloak.authenticated) {
        const profileData = await getUserProfile();
        setUserDetails({
          subscriptionPlan: profileData.subscriptionPlan,
          isProfileCompleted: profileData.isProfileCompleted,
          dailyUpload: profileData.dailyUpload,
        });
      }
    };
    getUserDetils();
  }, [keycloak.authenticated]);

  const completeProfile = (userData: UserDetails) => {
    setUserDetails((prev) => ({
      ...prev,
      subscriptionPlan: userData.subscriptionPlan,
      isProfileCompleted: userData.isProfileCompleted,
    }));
  };

  const increaseDailyUpload = () => {
    setUserDetails((prev) => ({
      ...prev,
      dailyUpload: prev.dailyUpload + 1,
    }));
  };

  return (
    <UserDetailsContext.Provider
      value={{
        userDetails,
        setUserDetails,
        completeProfile,
        increaseDailyUpload,
      }}
    >
      {children}
    </UserDetailsContext.Provider>
  );
};
