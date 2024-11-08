import { useState } from "react";
import { Photo, UpdatePhotoMetadata } from "../types/types";
import DeleteModal from "./DeleteModal";
import TagsInput from "./TagsInput";
import { useGetTags } from "../hooks/api/photoApiHooks";

type UpdatePhotoModalProps = {
  photo: Photo;
  onClose: () => void;
  onSubmit: (data: UpdatePhotoMetadata) => void;
  onDelete: () => void;
};

const UpdatePhotoModal = ({
  photo,
  onClose,
  onSubmit,
  onDelete,
}: UpdatePhotoModalProps) => {
  const { data: tags, isLoading } = useGetTags();

  const [updatePhotoMetadata, setUpdatePhotoMetadata] = useState({
    description: photo.description ?? "",
    tags: photo.tags ?? [],
  });
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

  const handleOnChange = (key: string, value: string) => {
    setUpdatePhotoMetadata({ ...updatePhotoMetadata, [key]: value });
  };

  const handleTagsOnChange = (tagsValue: string[]) => {
    setUpdatePhotoMetadata({ ...updatePhotoMetadata, tags: tagsValue });
  };

  const handleSubmit = () => {
    onSubmit({
      description: updatePhotoMetadata.description,
      tags: updatePhotoMetadata.tags,
    });
  };

  return (
    <div className="modal modal-open">
      <div className="modal-box">
        <h2 className="font-bold text-lg mb-4">Update photo</h2>

        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Description:</span>
          </label>
          <textarea
            className="textarea textarea-bordered"
            placeholder="Enter photo description"
            value={updatePhotoMetadata.description}
            onChange={(e) => handleOnChange("description", e.target.value)}
          />
        </div>

        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Tags:</span>
          </label>
          <TagsInput
            tags={tags || []}
            isLoading={isLoading}
            tagsValue={updatePhotoMetadata.tags}
            setTagsValue={(tagsValue) => handleTagsOnChange(tagsValue)}
          />
        </div>

        {/* Modal Actions */}
        <div className="modal-action flex flex-row justify-between">
          <button
            className="btn btn-secondary btn-outline"
            onClick={() => setIsDeleteModalOpen(true)}
          >
            Delete photo
          </button>
          <div className="flex flex-row gap-2">
            <button className="btn btn-primary" onClick={handleSubmit}>
              Update Picture
            </button>
            <button className="btn" onClick={onClose}>
              Cancel
            </button>
          </div>
        </div>
      </div>
      {isDeleteModalOpen && (
        <DeleteModal
          text={"Are you sure you want to delete this photo?"}
          onClose={() => setIsDeleteModalOpen(false)}
          onSubmit={onDelete}
        />
      )}
    </div>
  );
};

export default UpdatePhotoModal;
