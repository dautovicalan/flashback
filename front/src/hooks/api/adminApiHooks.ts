import { useMutation, useQuery } from "@tanstack/react-query";
import {
  deleteActionLogsByUser,
  deletePhotoAsAdmin,
  deleteUserAsAdmin,
  getActionLogs,
  getActionLogsByAction,
  getActionLogsByUser,
  getAllUsersAsAdmin,
  getPhotoByIdAsAdmin,
  getPhotosAsAdmin,
  getUserByIdAsAdmin,
  getUserDetailsByIdAsAdmin,
  getUsersAsAdmin,
  updatePhotoMetadataAsAdmin,
  updateUserDataAsAdmin,
} from "../../pages/AdminPage/api/adminApi";
import {
  LogAction,
  Photo,
  UpdatePhoto,
  KeycloakUser,
  UpdateUserData,
} from "../../types/types";

export const useGetUsersAsAdmin = (
  page: number,
  size: number,
  query: string
) => {
  return useQuery({
    queryKey: ["usersAsAdmin", page, size, query],
    queryFn: () => getUsersAsAdmin(page, size, query),
  });
};

export const useGetAllUsersAsAdmin = () => {
  return useQuery({
    queryKey: ["allUsersAsAdmin"],
    queryFn: () => getAllUsersAsAdmin(),
  });
};

export const useGetUserByIdAsAdmin = (userId: string) => {
  return useQuery({
    queryKey: ["userByIdAsAdmin", userId],
    queryFn: () => getUserByIdAsAdmin(userId),
  });
};

export const useGetUserDetailsByIdAsAdmin = (userId: string) => {
  return useQuery({
    queryKey: ["userDetailsByIdAsAdmin", userId],
    queryFn: () => getUserDetailsByIdAsAdmin(userId),
  });
};

export const useUpdateUserAsAdmin = () => {
  return useMutation<
    KeycloakUser,
    Error,
    { userId: string; userData: UpdateUserData }
  >({
    mutationKey: ["updateUserAsAdmin"],
    mutationFn: async ({ userId, userData }) => {
      const response = await updateUserDataAsAdmin(userId, userData);

      return response;
    },
  });
};

export const useDeleteUserAsAdmin = () => {
  return useMutation({
    mutationKey: ["deleteUser"],
    mutationFn: async (userId: string) => {
      const responseStatus = await deleteUserAsAdmin(userId);

      if (responseStatus === 200) {
        return undefined;
      } else {
        throw new Error("Failed to delete user account");
      }
    },
  });
};

export const useGetPhotosAsAdmin = (
  page: number,
  size: number,
  query: string
) => {
  return useQuery({
    queryKey: ["photosAsAdmin", page, size, query],
    queryFn: () => getPhotosAsAdmin(page, size, query),
  });
};

export const useGetPhotoByIdAsAdmin = (photoId: string) => {
  return useQuery({
    queryKey: ["photoByIdAsAdmin", photoId],
    queryFn: () => getPhotoByIdAsAdmin(photoId),
  });
};

export const useUpdatePhotoMetadataAsAdmin = () => {
  return useMutation<
    Photo,
    Error,
    { photoId: string; updatePhoto: UpdatePhoto }
  >({
    mutationKey: ["updatePhotoMetadataAsAdmin"],
    mutationFn: async ({ photoId, updatePhoto }) => {
      const response = await updatePhotoMetadataAsAdmin(photoId, updatePhoto);

      return response;
    },
  });
};

export const useDeletePhotoAsAdmin = () => {
  return useMutation({
    mutationKey: ["deletePhotoAsAdmin"],
    mutationFn: async (photoId: string) => {
      const responseStatus = await deletePhotoAsAdmin(photoId);

      if (responseStatus === 200) {
        return undefined;
      } else {
        throw new Error("Failed to delete photo");
      }
    },
  });
};

export const useGetActionLogs = () => {
  return useQuery({
    queryKey: ["actionLogs"],
    queryFn: () => getActionLogs(),
  });
};

export const useGetActionLogsByUser = (userId: string) => {
  return useQuery({
    queryKey: ["actionLogsByUser", userId],
    queryFn: () => getActionLogsByUser(userId),
  });
};

export const useGetActionLogsByAction = (action: LogAction) => {
  return useQuery({
    queryKey: ["actionLogsByAction", action],
    queryFn: () => getActionLogsByAction(action),
  });
};

export const useDeleteActionLogsByUser = () => {
  return useMutation({
    mutationKey: ["deleteActionLogsByUser"],
    mutationFn: async (userId: string) => {
      const responseStatus = await deleteActionLogsByUser(userId);

      if (responseStatus === 200) {
        return undefined;
      } else {
        throw new Error("Failed to delete action logs");
      }
    },
  });
};
