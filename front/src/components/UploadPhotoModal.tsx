import React, { useState, ChangeEvent, SyntheticEvent } from "react";
import { PhotoFormat, PhotoMetadata, PhotoUploadRequest } from "../types/types";
import { toast } from "react-toastify";
import PhotoFormatSelect from "./PhotoFormatSelect";
import TagsInput from "./TagsInput";
import { useGetTags } from "../hooks/api/photoApiHooks";

type UploadPhotoModalProps = {
  onClose: () => void;
  onSubmit: (uploadRequest: PhotoUploadRequest) => void;
};

const validPhotoMetadata = (metadata: PhotoMetadata) => {
  return metadata.width <= 0 || metadata.height <= 0;
};

const UploadPhotoModal: React.FC<UploadPhotoModalProps> = ({
  onClose,
  onSubmit,
}) => {
  const { data: tags, isLoading } = useGetTags();

  const [selectedPhoto, setSelectedPhoto] = useState<File | null>(null);
  const [photoDescription, setPhotoDescription] = useState("");
  const [photoTags, setPhotoTags] = useState<string[]>([]);
  const [photoMetadata, setPhotoMetaData] = useState<PhotoMetadata>({
    width: 150,
    height: 150,
    format: PhotoFormat.PNG,
  });
  const [uploadAsOriginal, setUploadAsOriginal] = useState(true);
  const [isUploading, setIsUploading] = useState(false);

  const handleUploadInfoChange = (key: string, value: string) => {
    setPhotoMetaData({ ...photoMetadata, [key]: value });
  };

  const handlePhotoChange = (event: ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files[0]) {
      setSelectedPhoto(event.target.files[0]);
    }
  };

  const handleSubmit = (e: SyntheticEvent) => {
    e.preventDefault();
    if (!selectedPhoto) {
      return toast.error("Please select a photo to upload");
    }
    if (validPhotoMetadata(photoMetadata)) {
      return toast.error("Please enter valid photo dimensions");
    }
    setIsUploading(true);
    onSubmit({
      file: selectedPhoto,
      description: photoDescription,
      tags: photoTags,
      metadata: uploadAsOriginal ? undefined : photoMetadata,
    });
    setIsUploading(false);
  };

  return (
    <div className="modal modal-open">
      <div className="modal-box">
        <h2 className="font-bold text-lg mb-4">Upload Photo</h2>

        {/* Photo Upload Input */}
        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Select Photo</span>
          </label>
          <input
            type="file"
            className="file-input file-input-bordered file-input-primary w-full"
            accept="image/*"
            onChange={handlePhotoChange}
          />
        </div>

        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Upload as original</span>
          </label>
          <input
            type="checkbox"
            className="checkbox ml-1"
            checked={uploadAsOriginal}
            onChange={() => setUploadAsOriginal((prev) => !prev)}
          />
        </div>

        {/* Description Input */}
        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Description</span>
          </label>
          <textarea
            className="textarea textarea-bordered"
            placeholder="Enter photo description..."
            value={photoDescription}
            onChange={(e) => setPhotoDescription(e.target.value)}
          />
        </div>

        <div className="form-control mb-4">
          <TagsInput
            tags={tags || []}
            isLoading={isLoading}
            tagsValue={photoTags}
            setTagsValue={(tagsValue) => setPhotoTags(tagsValue)}
          />
        </div>

        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Picture width:</span>
          </label>
          <input
            disabled={uploadAsOriginal}
            type="number"
            className="input input-bordered"
            placeholder="Enter width or leave empty for default"
            value={photoMetadata.width}
            min={10}
            max={10000}
            onChange={(e) => handleUploadInfoChange("width", e.target.value)}
          />
        </div>

        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Picture height</span>
          </label>
          <input
            disabled={uploadAsOriginal}
            type="number"
            className="input input-bordered"
            placeholder="Enter height or leave empty for default"
            value={photoMetadata.height}
            min={10}
            max={10000}
            onChange={(e) => handleUploadInfoChange("height", e.target.value)}
          />
        </div>

        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Picture format</span>
          </label>
          <PhotoFormatSelect
            disabled={uploadAsOriginal}
            value={photoMetadata.format}
            onChange={(e) => handleUploadInfoChange("format", e.target.value)}
          />
        </div>

        {/* Modal Actions */}
        <div className="modal-action">
          <button
            className="btn btn-primary"
            onClick={handleSubmit}
            disabled={isUploading}
          >
            Upload
          </button>
          <button className="btn" onClick={onClose} disabled={isUploading}>
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
};

export default UploadPhotoModal;
