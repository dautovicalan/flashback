import axiosInstance from "../../../api/axiosInstance";
import { SubscriptionPlan, UserDetails } from "../../../types/types";

export const getUserProfile = async (): Promise<UserDetails> => {
  const response = await axiosInstance.get<UserDetails>(`/user/me`);
  return response.data;
};

export const changeUserSubscriptionPlan = async (
  plan: SubscriptionPlan
): Promise<UserDetails> => {
  const response = await axiosInstance.put<UserDetails>(`/user/change-plan`, {
    subscriptionPlan: plan,
  });
  return response.data;
};

export const deleteUserAccount = async (): Promise<number> => {
  const response = await axiosInstance.delete(`/user/`);
  return response.status;
};
