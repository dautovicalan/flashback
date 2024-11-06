import { useMutation, useQuery } from "@tanstack/react-query";
import {
  changeUserSubscriptionPlan,
  deleteUserAccount,
  getUserProfile,
} from "../../pages/ProfilePage/api/profileApi";
import {
  CompleteProfile,
  SubscriptionPlan,
  UserDetails,
} from "../../types/types";
import { completeProfileApi } from "../../pages/CompleteProfilePage/api/completeProfileApi";
import { useAuthContext } from "../useAuthContext";
import { useUserDetailsContext } from "../useUserDetailsContext";

export const useFetchUserData = () => {
  const { user } = useAuthContext();

  return useQuery({
    queryKey: ["userInfo"],
    queryFn: () => getUserProfile(),
    enabled: !!user,
  });
};

export const useChangeUserSubscriptionPlan = () => {
  const { completeProfile } = useUserDetailsContext();

  return useMutation<UserDetails, Error, SubscriptionPlan>({
    mutationKey: ["changeSubscriptionPlan"],
    mutationFn: async (newPlan) => {
      return await changeUserSubscriptionPlan(newPlan);
    },
    onSuccess: (data) => {
      completeProfile(data);
    },
  });
};

export const useCompleteProfile = () => {
  const { completeProfile } = useUserDetailsContext();

  return useMutation<UserDetails, Error, CompleteProfile>({
    mutationKey: ["completeProfile"],
    mutationFn: async (data) => {
      const response = await completeProfileApi(data);
      return response;
    },
    onSuccess(data) {
      completeProfile(data);
    },
  });
};

export const useDeleteUserAccount = () => {
  const { logout } = useAuthContext();
  return useMutation({
    mutationKey: ["deleteUser"],
    mutationFn: async () => {
      const responseStatus = await deleteUserAccount();

      if (responseStatus === 204) {
        return undefined;
      } else {
        throw new Error("Failed to delete user account");
      }
    },
    onSettled: () => {
      logout();
    },
  });
};
