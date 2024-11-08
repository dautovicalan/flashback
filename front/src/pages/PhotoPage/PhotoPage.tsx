import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Tags from "../../components/Tags";
import LoadingScreen from "../../components/LoadingScreen";
import DownloadPhotoModal from "../../components/DownloadPhotoModal";
import {
  useDeletePhoto,
  useDownloadPhoto,
  useGetPhotoById,
  useUpdatePhotoMetadata,
} from "../../hooks/api/photoApiHooks";
import { ErrorScreen } from "../../components/ErrorBoundary";
import { toast } from "react-toastify";
import { PhotoFilters, UpdatePhotoMetadata } from "../../types/types";
import { toFormattedDate } from "../../utils/dateUtils";
import UpdatePhotoModal from "../../components/UpdatePhotoModal";
import { useAuthContext } from "../../hooks/useAuthContext";
import Tooltip from "../../components/shared/Tooltip";

const PhotoPage = () => {
  const { id } = useParams();
  const { user } = useAuthContext();
  const navigate = useNavigate();

  const downloadPhotoMutation = useDownloadPhoto();
  const updatePhotoMetadataMutation = useUpdatePhotoMetadata();
  const deletePhotoMutation = useDeletePhoto();

  const {
    data: photo,
    isLoading,
    isError,
    error,
    refetch,
  } = useGetPhotoById(id as string);

  const [isDownloadModalOpen, setIsDownloadModalOpen] = useState(false);
  const [isUpdatePhotoModalOpen, setIsUpdatePhotoModalOpen] = useState(false);

  const handleOpenDownloadModal = () => {
    if (!user) {
      toast.error("You need to be logged in to download photos");
      return;
    }
    setIsDownloadModalOpen(true);
  };

  const handleDownloadModalClose = () => {
    setIsDownloadModalOpen(false);
  };

  const handleDownloadSubmit = (photoFilters?: PhotoFilters) => {
    downloadPhotoMutation.mutate(
      {
        photoId: id as string,
        photoFilters: photoFilters,
      },
      {
        onSuccess: (photoDownloadResponse) => {
          const url = window.URL.createObjectURL(photoDownloadResponse.blob);
          const link = document.createElement("a");
          link.href = url;
          link.setAttribute("download", photoDownloadResponse.filename);
          document.body.appendChild(link);
          link.click();
          link.parentNode?.removeChild(link);
          handleDownloadModalClose();
        },
        onError: (error) => {
          toast.error(error.message);
        },
      }
    );
  };

  const handleOpenUpdatePhotoModal = () => {
    setIsUpdatePhotoModalOpen(true);
  };

  const handleUpdatePhotoModalClose = () => {
    setIsUpdatePhotoModalOpen(false);
  };

  const handleUpdatePhotoMetadata = (data: UpdatePhotoMetadata) => {
    updatePhotoMetadataMutation.mutate(
      {
        photoId: id as string,
        metadata: data,
      },
      {
        onSuccess() {
          handleUpdatePhotoModalClose();
          toast.success("Photo updated successfully!");
          refetch();
        },
        onError(error) {
          toast.error(error.message);
        },
      }
    );
  };

  const handleDeletePhoto = () => {
    deletePhotoMutation.mutate(id as string, {
      onSuccess() {
        toast.success("Photo deleted successfully!");
        navigate("/");
      },
      onError(error) {
        toast.error(error.message);
      },
    });
  };

  return (
    <>
      {isLoading && <LoadingScreen />}
      {isError && <ErrorScreen message={error.message} />}
      {photo && (
        <div className="p-8 bg-base-200 min-h-screen">
          <div className="max-w-4xl mx-auto">
            {/* Photo Card */}
            <div className="card bg-white shadow-xl p-6 mb-8">
              <div className="flex items-center justify-between">
                <div className="flex items-center space-x-6">
                  {/* Photo image */}
                  <div className="w-48 h-48 bg-gray-100 rounded-lg overflow-hidden">
                    <img
                      src={photo.url}
                      alt={photo.description}
                      className="object-cover w-full h-full hover:opacity-80 cursor-pointer"
                      onClick={() => window.open(photo.url, "_blank")}
                    />
                  </div>
                  {/* Photo details */}
                  <div>
                    <h1 className="text-2xl font-bold">
                      Description:{" "}
                      {photo?.description && photo.description.length > 0 ? (
                        <Tooltip text={photo.description} maxLen={25} />
                      ) : (
                        "No description"
                      )}
                    </h1>
                    <p className="text-gray-500 font-bold">
                      Author: {photo.author}
                    </p>
                    <p className="text-gray-500">Width: {photo.width} px</p>
                    <p className="text-gray-500">Height: {photo.height} px</p>
                    <p className="text-gray-500">
                      Upload date: {toFormattedDate(photo.uploadDate)}
                    </p>
                    <p className="text-gray-500">
                      Format:{" "}
                      <span className="badge badge-accent">{photo.format}</span>
                    </p>
                    <div className="mt-2">
                      <Tags tags={photo.tags} />
                    </div>
                  </div>
                </div>
                {/* Download button */}
                <div className="flex flex-col gap-2 items-end">
                  <button
                    className="btn btn-primary"
                    onClick={handleOpenDownloadModal}
                  >
                    Download
                  </button>
                  {user && user.id === photo.ownerId && (
                    <button
                      className="btn btn-primary"
                      onClick={handleOpenUpdatePhotoModal}
                    >
                      Update Photo
                    </button>
                  )}
                </div>
              </div>
            </div>
          </div>
          {isDownloadModalOpen && user && (
            <DownloadPhotoModal
              onClose={handleDownloadModalClose}
              onSubmit={handleDownloadSubmit}
              photo={photo}
            />
          )}

          {isUpdatePhotoModalOpen && user && (
            <UpdatePhotoModal
              photo={photo}
              onClose={handleUpdatePhotoModalClose}
              onSubmit={handleUpdatePhotoMetadata}
              onDelete={handleDeletePhoto}
            />
          )}
        </div>
      )}
    </>
  );
};

export default PhotoPage;
