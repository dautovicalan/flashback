import { useState } from "react";
import { Photo, UpdatePhoto } from "../../types/types";
import { useGetTags } from "../../hooks/api/photoApiHooks";
import TagsInput from "../TagsInput";

type UpdatePhotoModalProps = {
  photo: Photo;
  onClose: () => void;
  onSubmit: (data: UpdatePhoto) => void;
};

const UpdatePhotoAsAdminModal = ({
  photo,
  onClose,
  onSubmit,
}: UpdatePhotoModalProps) => {
  const { data: tags, isLoading } = useGetTags();

  const [updatePhotoMetadata, setUpdatePhotoMetadata] = useState({
    description: photo.description ?? "",
    width: photo.width ?? "",
    height: photo.height ?? "",
    format: photo.format ?? "JPG",
    tags: photo.tags || [],
  });

  const handleOnChange = (key: string, value: string) => {
    setUpdatePhotoMetadata({ ...updatePhotoMetadata, [key]: value });
  };

  const handleTagsOnChange = (tagsValue: string[]) => {
    setUpdatePhotoMetadata({ ...updatePhotoMetadata, tags: tagsValue });
  };

  const handleSubmit = () => {
    onSubmit({
      description: updatePhotoMetadata.description,
      width: updatePhotoMetadata.width,
      height: updatePhotoMetadata.height,
      format: updatePhotoMetadata.format,
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
            <span className="label-text">Width:</span>
          </label>
          <input
            type="text"
            className="input input-bordered"
            placeholder="Enter photo tags"
            value={updatePhotoMetadata.width}
            onChange={(e) => handleOnChange("width", e.target.value)}
          />
        </div>

        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Height:</span>
          </label>
          <input
            type="text"
            className="input input-bordered"
            placeholder="Enter photo tags"
            value={updatePhotoMetadata.height}
            onChange={(e) => handleOnChange("height", e.target.value)}
          />
        </div>

        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Format:</span>
          </label>
          <select
            className="select select-bordered"
            value={updatePhotoMetadata.format}
            onChange={(e) => handleOnChange("format", e.target.value)}
          >
            <option value="JPG">JPG</option>
            <option value="JPEG">JPEG</option>
            <option value="PNG">PNG</option>
            <option value="WEBP">WEBP</option>
            <option value="BMP">BMP</option>
          </select>
        </div>

        <div className="form-control mb-4">
          <TagsInput
            tags={tags || []}
            isLoading={isLoading}
            tagsValue={updatePhotoMetadata.tags}
            setTagsValue={(tagsValue) => handleTagsOnChange(tagsValue)}
          />
        </div>

        {/* Modal Actions */}
        <div className="modal-action flex flex-row justify-between">
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
    </div>
  );
};

export default UpdatePhotoAsAdminModal;
