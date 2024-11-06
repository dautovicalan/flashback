import { useNavigate, useParams } from "react-router-dom";
import {
  useDeletePhotoAsAdmin,
  useGetPhotoByIdAsAdmin,
  useUpdatePhotoMetadataAsAdmin,
} from "../../../hooks/api/adminApiHooks";
import LoadingScreen from "../../../components/LoadingScreen";
import { ErrorScreen } from "../../../components/ErrorBoundary";
import Tags from "../../../components/Tags";
import { toFormattedDate } from "../../../utils/dateUtils";
import { useState } from "react";
import DeleteModal from "../../../components/DeleteModal";
import { toast } from "react-toastify";
import { UpdatePhoto } from "../../../types/types";
import UpdatePhotoAsAdminModal from "../../../components/Admin/UpdatePhotoAsAdminModal";

const AdminPhotoInformationPage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const {
    data: photo,
    isLoading,
    error,
    refetch,
  } = useGetPhotoByIdAsAdmin(id!);
  const updatePhotoMetadataMutation = useUpdatePhotoMetadataAsAdmin();
  const deletePhotoMutation = useDeletePhotoAsAdmin();

  const [isUpdatePhotoMetadataDialogOpen, setIsUpdatePhotoMetadataDialogOpen] =
    useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);

  const handleUpdatePhotoMetadata = (updatePhoto: UpdatePhoto) => {
    if (!id) return;

    updatePhotoMetadataMutation.mutate(
      { photoId: id, updatePhoto },
      {
        onSuccess: () => {
          setIsUpdatePhotoMetadataDialogOpen(false);
          toast.success("Photo updated successfully!");
          refetch();
        },
        onError: (error) => {
          toast.error(error.message);
        },
      }
    );
  };

  const handleDeletePhoto = () => {
    if (!id) return;

    deletePhotoMutation.mutate(id, {
      onSuccess: () => {
        setIsDeleteDialogOpen(false);
        toast.success("Photo deleted successfully!");
        navigate("/admin/photos");
      },
      onError: (error) => {
        toast.error(error.message);
      },
    });
  };

  return (
    <>
      {isLoading && <LoadingScreen />}
      {error && <ErrorScreen message={error.message} />}
      {photo && (
        <div className="min-h-screen">
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
                      Description: {photo?.description}
                    </h1>
                    <p className="text-gray-500">Author: {photo.author}</p>
                    <p className="text-gray-500">Width: {photo.width} px</p>
                    <p className="text-gray-500">Height: {photo.height} px</p>
                    <p className="text-gray-500">
                      Upload date: {toFormattedDate(photo.uploadDate)}
                    </p>
                    <p className="text-gray-500">Format: {photo.format}</p>
                    <div className="mt-2">
                      <Tags tags={photo.tags} />
                    </div>
                  </div>
                </div>
              </div>
              <div className="flex justify-end gap-5 mt-5">
                <button
                  className="btn btn-primary"
                  onClick={() => setIsUpdatePhotoMetadataDialogOpen(true)}
                >
                  Modify Photo
                </button>
                <button
                  className="btn btn-error"
                  onClick={() => setIsDeleteDialogOpen(true)}
                >
                  Delete Photo
                </button>
              </div>
            </div>
          </div>

          {isUpdatePhotoMetadataDialogOpen && (
            <UpdatePhotoAsAdminModal
              photo={photo}
              onClose={() => setIsUpdatePhotoMetadataDialogOpen(false)}
              onSubmit={handleUpdatePhotoMetadata}
            />
          )}

          {isDeleteDialogOpen && (
            <DeleteModal
              onClose={() => setIsDeleteDialogOpen(false)}
              onSubmit={handleDeletePhoto}
              text={"Are you sure you want to delete this photo?"}
            />
          )}
        </div>
      )}
    </>
  );
};

export default AdminPhotoInformationPage;
