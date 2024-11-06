import axiosInstance from "../../../api/axiosInstance";
import {
  LogAction,
  LogEntry,
  Pageable,
  Photo,
  UpdatePhoto,
  KeycloakUser,
  UpdateUserData,
  UserDetails,
} from "../../../types/types";

export const getUsersAsAdmin = async (
  page: number,
  size: number,
  query: string
) => {
  const response = await axiosInstance.get<Pageable<KeycloakUser>>(
    `/admin/users?page=${page}&size=${size}&query=${query}`
  );
  return response.data;
};

export const getAllUsersAsAdmin = async (): Promise<KeycloakUser[]> => {
  const response = await axiosInstance.get<KeycloakUser[]>("/admin/users/all");
  return response.data;
};

export const getUserByIdAsAdmin = async (userId: string) => {
  const response = await axiosInstance.get<KeycloakUser>(
    `/admin/users/${userId}`
  );
  return response.data;
};

export const getUserDetailsByIdAsAdmin = async (userId: string) => {
  const response = await axiosInstance.get<UserDetails>(
    `/admin/users/${userId}/details`
  );
  return response.data;
};

export const updateUserDataAsAdmin = async (
  userId: string,
  userUpdateData: UpdateUserData
) => {
  const response = await axiosInstance.put<KeycloakUser>(
    `/admin/users/${userId}`,
    userUpdateData
  );
  return response.data;
};

export const deleteUserAsAdmin = async (userId: string) => {
  const response = await axiosInstance.delete<number>(`/admin/users/${userId}`);
  return response.status;
};

export const getPhotosAsAdmin = async (
  page = 0,
  size = 10,
  query?: string
): Promise<Pageable<Photo>> => {
  const response = await axiosInstance.get(
    `/admin/photos?page=${page}&size=${size}&query=${query}`
  );
  return response.data;
};

export const getPhotoByIdAsAdmin = async (photoId: string): Promise<Photo> => {
  const response = await axiosInstance.get<Photo>(`/admin/photos/${photoId}`);
  return response.data;
};

export const updatePhotoMetadataAsAdmin = async (
  photoId: string,
  updatePhoto: UpdatePhoto
): Promise<Photo> => {
  const response = await axiosInstance.put(
    `/admin/photos/${photoId}`,
    updatePhoto
  );
  return response.data;
};

export const deletePhotoAsAdmin = async (photoId: string): Promise<number> => {
  const response = await axiosInstance.delete(`/admin/photos/${photoId}`);
  return response.status;
};

export const getActionLogs = async (): Promise<LogEntry[]> => {
  const response = await axiosInstance.get<LogEntry[]>("/statistics/logs");
  return response.data;
};

export const getActionLogsByUser = async (
  userId: string
): Promise<LogEntry[]> => {
  const response = await axiosInstance.get<LogEntry[]>(
    `/statistics/logs/${userId}`
  );
  return response.data;
};

export const getActionLogsByAction = async (logAction: LogAction) => {
  const response = await axiosInstance.get<LogEntry[]>(
    `/statistics/logs/action/${logAction}`
  );
  return response.data;
};

export const deleteActionLogsByUser = async (userId: string) => {
  const response = await axiosInstance.delete(`/statistics/logs/${userId}`);
  return response.status;
};
