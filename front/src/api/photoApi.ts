import {
  Pageable,
  Photo,
  PhotoDownloadResponse,
  PhotoFilters,
  PhotoMetadata,
  UpdatePhotoMetadata,
  UploadResult,
} from "../types/types";
import axiosInstance from "./axiosInstance";

export const getPhotos = async (
  page = 0,
  size = 10,
  query: string,
  fromDate: string,
  toDate: string
): Promise<Pageable<Photo>> => {
  const response = await axiosInstance.get(
    `/photos/?page=${page}&size=${size}&query=${query}&fromDate=${fromDate}&toDate=${toDate}`
  );
  return response.data;
};

export const getPhotoById = async (photoId: string): Promise<Photo> => {
  const response = await axiosInstance.get<Photo>(`/photos/${photoId}`);
  return response.data;
};

export const getPhotosByUserId = async (userId: string): Promise<Photo[]> => {
  const response = await axiosInstance.get<Photo[]>(`/photos/user/${userId}`);
  return response.data;
};

export const updatePhotoMetadata = async (
  photoId: string,
  updatePhotoMetadata: UpdatePhotoMetadata
): Promise<Photo> => {
  const response = await axiosInstance.put(
    `/photos/${photoId}`,
    updatePhotoMetadata
  );
  return response.data;
};

export const deletePhoto = async (photoId: string): Promise<number> => {
  const response = await axiosInstance.delete(`/photos/${photoId}`);
  return response.status;
};

export const downloadPhoto = async (
  photoId: string,
  photoFilters?: PhotoFilters
): Promise<PhotoDownloadResponse> => {
  const response = await axiosInstance.post(
    `/download/${photoId}`,
    photoFilters,
    {
      responseType: "blob",
    }
  );
  const contentDisposition = response.headers["content-disposition"];
  const filename = (contentDisposition as string).split("=")[1].trim();

  return {
    blob: response.data,
    filename: filename,
  };
};

export const uploadPhotoApi = async (
  file: File,
  description: string,
  tags: string[],
  photoMetadata?: PhotoMetadata
): Promise<UploadResult> => {
  const formData = new FormData();
  if (photoMetadata) {
    formData.append(
      "photoMetadataDto",
      new Blob([JSON.stringify(photoMetadata)], { type: "application/json" })
    );
  }
  formData.append("file", file);
  formData.append("description", description);
  formData.append(
    "tags",
    new Blob([JSON.stringify(tags)], { type: "application/json" })
  );
  const response = await axiosInstance.post("/upload/", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
  return response.data;
};

export const getTags = async (): Promise<string[]> => {
  const response = await axiosInstance.get<string[]>("/tags/");
  return response.data;
};
