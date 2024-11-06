import axiosInstance from "../../../api/axiosInstance";
import { CompleteProfile, UserDetails } from "../../../types/types";

export const completeProfileApi = async (
  data: CompleteProfile
): Promise<UserDetails> => {
  const response = await axiosInstance.put("/user/complete-profile", data);
  return response.data;
};
