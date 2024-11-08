export enum Role {
  ADMIN = "ADMIN",
  USER = "USER",
}

export enum SubscriptionPlan {
  FREE = "FREE",
  PRO = "PRO",
  GOLD = "GOLD",
}

export enum PhotoFormat {
  JPEG = "JPEG",
  PNG = "PNG",
  BMP = "BMP",
  JPG = "JPG",
  GIF = "GIF",
  WBMP = "WBMP",
}

export type KeycloakUser = {
  id: string;
  username: string;
  email?: string;
  firstName: string | "";
  lastName: string | "";
  role: Role;
};

export type UserDetails = {
  subscriptionPlan: SubscriptionPlan;
  isProfileCompleted: boolean;
  dailyUpload: number;
};

export type UpdateUserData = {
  firstName?: string;
  lastName?: string;
  subscriptionPlan: SubscriptionPlan;
  resetDailyUploads: boolean;
};

export type CompleteProfile = {
  subscriptionPlan: SubscriptionPlan;
};

export type Sort = {
  sorted: boolean;
  unsorted: boolean;
  empty: boolean;
};

export type Pageable<T> = {
  content: T[];
  empty: boolean;
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  size: number;
  totalElements: number;
  totalPages: number;
};

export type Photo = {
  id: number;
  description?: string;
  tags?: string[];
  url: string;
  width: number;
  height: number;
  author: string;
  ownerId: string;
  uploadDate: string;
  format: PhotoFormat;
};

export type PhotoMetadata = {
  width: number;
  height: number;
  format: PhotoFormat;
};

export type UpdatePhoto = {
  description: string;
  tags: string[];
  height: number;
  width: number;
  format: PhotoFormat;
};

export type UpdatePhotoMetadata = {
  description?: string;
  tags?: string[];
};

export type PhotoUploadRequest = {
  file: File;
  description: string;
  tags: string[];
  metadata?: PhotoMetadata;
};

export type PhotoDownloadResponse = {
  blob: Blob;
  filename: string;
};

export type UploadResult = {
  name: string;
  key: string;
  uploadDate: string;
};

export type PhotoFilters = {
  width: number;
  height: number;
  sepia: number;
  blur: number;
  format: PhotoFormat;
};

export enum LogAction {
  UPDATE_USER,
  COMPLETE_PROFILE,
  UPLOAD_PHOTO,
  DELETE_PHOTO,
  UPDATE_PHOTO,
  DOWNLOAD_PHOTO,
  DELETE_ACCOUNT,
}

export type LogEntry = {
  id: number;
  userId: string;
  action: string;
  timestamp: string;
  logAction: LogAction;
};
