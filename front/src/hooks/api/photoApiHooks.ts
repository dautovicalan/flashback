import { useMutation, useQuery } from "@tanstack/react-query";
import {
  Photo,
  PhotoDownloadResponse,
  PhotoFilters,
  PhotoUploadRequest,
  UpdatePhotoMetadata,
  UploadResult,
} from "../../types/types";
import {
  downloadPhoto,
  getPhotos,
  getPhotoById,
  uploadPhotoApi,
  deletePhoto,
  updatePhotoMetadata,
  getPhotosByUserId,
  getTags,
} from "../../api/photoApi";
import { Dayjs } from "dayjs";
import { useUserDetailsContext } from "../useUserDetailsContext";

export const useGetPhotos = (
  page: number,
  size: number,
  query: string,
  fromDate: Dayjs | null,
  toDate: Dayjs | null
) => {
  return useQuery({
    queryKey: ["photos", page, size, query, fromDate, toDate],
    queryFn: () =>
      getPhotos(
        page,
        size,
        query,
        fromDate?.format("YYYY-MM-DD") ?? "",
        toDate?.format("YYYY-MM-DD") ?? ""
      ),
  });
};

export const useGetPhotoById = (photoId: string) => {
  return useQuery({
    queryKey: ["photo", photoId],
    queryFn: () => getPhotoById(photoId),
  });
};

export const useGetPhotosByUserId = (userId: string) => {
  return useQuery({
    queryKey: ["photosByUserId", userId],
    queryFn: () => getPhotosByUserId(userId),
  });
};

export const useUpdatePhotoMetadata = () => {
  return useMutation<
    Photo,
    Error,
    { photoId: string; metadata: UpdatePhotoMetadata }
  >({
    mutationKey: ["updatePhotoMetadata"],
    mutationFn: async ({ photoId, metadata }) => {
      const response = await updatePhotoMetadata(photoId, metadata);
      return response;
    },
  });
};

export const useDeletePhoto = () => {
  return useMutation<number, Error, string>({
    mutationKey: ["deletePhoto"],
    mutationFn: async (photoId: string) => {
      const response = await deletePhoto(photoId);
      return response;
    },
  });
};

export const useUploadPhoto = () => {
  const { increaseDailyUpload } = useUserDetailsContext();
  return useMutation<UploadResult, Error, PhotoUploadRequest>({
    mutationKey: ["uploadPhoto"],
    mutationFn: async (uploadRequest: PhotoUploadRequest) => {
      const repsonse = await uploadPhotoApi(
        uploadRequest.file,
        uploadRequest.description,
        uploadRequest.tags,
        uploadRequest.metadata
      );
      return repsonse;
    },
    onSuccess: () => {
      increaseDailyUpload();
    },
  });
};

export const useDownloadPhoto = () => {
  return useMutation<
    PhotoDownloadResponse,
    Error,
    { photoId: string; photoFilters?: PhotoFilters }
  >({
    mutationKey: ["downloadPhoto"],
    mutationFn: async ({ photoId, photoFilters }) => {
      const response = await downloadPhoto(photoId, photoFilters);
      return response;
    },
  });
};

export const useGetTags = () => {
  return useQuery({
    queryKey: ["tags"],
    queryFn: () => getTags(),
  });
};
