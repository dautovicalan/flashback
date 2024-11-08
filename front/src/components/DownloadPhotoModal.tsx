import React, { useState } from "react";
import { PhotoFilters, Photo } from "../types/types";
import PhotoFormatSelect from "./PhotoFormatSelect";

type DownloadPhotoModalProps = {
  photo: Photo;
  onClose: () => void;
  onSubmit: (photoFilters?: PhotoFilters) => void;
};

const DownloadPhotoModal: React.FC<DownloadPhotoModalProps> = ({
  photo,
  onClose,
  onSubmit,
}) => {
  const [photoDownloadFilters, setPhotoDownloadFilters] =
    useState<PhotoFilters>({
      width: photo.width,
      height: photo.height,
      sepia: 0,
      blur: 0,
      format: photo.format,
    });
  const [dowloadAsOriginal, setDownloadAsOriginal] = useState(true);
  const [isDownloading, setIsDownloading] = useState(false);

  const handleOnChange = (key: string, value: string) => {
    setPhotoDownloadFilters({ ...photoDownloadFilters, [key]: value });
  };

  const handleSubmit = () => {
    setIsDownloading(true);
    onSubmit(dowloadAsOriginal ? undefined : photoDownloadFilters);
    setIsDownloading(false);
  };

  return (
    <div className="modal modal-open">
      <div className="modal-box">
        <h2 className="font-bold text-lg mb-4">Download Photo</h2>

        <figure className="w-full">
          <img src={photo.url} width={200} height={200} />
        </figure>

        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Download as original</span>
          </label>
          <input
            type="checkbox"
            className="checkbox ml-1"
            checked={dowloadAsOriginal}
            onChange={() => setDownloadAsOriginal((prev) => !prev)}
          />
        </div>

        {/* Description Input */}
        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Custom photo width</span>
          </label>
          <input
            disabled={dowloadAsOriginal}
            type="number"
            min={10}
            max={10000}
            className="input input-bordered"
            placeholder="Enter photo height or leave blank for original size"
            value={photoDownloadFilters.width}
            onChange={(e) => handleOnChange("width", e.target.value)}
          />
        </div>

        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Custom photo height</span>
          </label>
          <input
            disabled={dowloadAsOriginal}
            type="number"
            min={10}
            max={10000}
            className="input input-bordered"
            placeholder="Enter photo height or leave blank for original size"
            value={photoDownloadFilters.height}
            onChange={(e) => handleOnChange("height", e.target.value)}
          />
        </div>

        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Set Sepia</span>
            <span className="text-xs text-gray-500 ml-1">
              {photoDownloadFilters.sepia}%
            </span>
          </label>
          <input
            disabled={dowloadAsOriginal}
            type="range"
            min={0}
            max={100}
            value={photoDownloadFilters.sepia}
            onChange={(e) => handleOnChange("sepia", e.target.value)}
            className="range"
          />
        </div>

        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Set blur</span>
            <span className="text-xs text-gray-500 ml-1">
              {photoDownloadFilters.blur}%
            </span>
          </label>
          <input
            disabled={dowloadAsOriginal}
            type="range"
            min={0}
            max={100}
            value={photoDownloadFilters.blur}
            onChange={(e) => handleOnChange("blur", e.target.value)}
            className="range"
          />
        </div>
        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Photo format</span>
          </label>
          <PhotoFormatSelect
            disabled={dowloadAsOriginal}
            value={photoDownloadFilters.format}
            onChange={(e) => handleOnChange("format", e.target.value)}
          />
        </div>

        {/* Modal Actions */}
        <div className="modal-action">
          <button
            className="btn btn-primary"
            onClick={handleSubmit}
            disabled={isDownloading}
          >
            Download
          </button>
          <button className="btn" onClick={onClose} disabled={isDownloading}>
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
};

export default DownloadPhotoModal;
