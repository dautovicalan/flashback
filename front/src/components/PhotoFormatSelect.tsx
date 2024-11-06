import React from "react";
import { PhotoFormat } from "../types/types";

type PhotoFormatSelectProps = {
  value: PhotoFormat;
  onChange: (event: React.ChangeEvent<HTMLSelectElement>) => void;
  disabled?: boolean;
};

const PhotoFormatSelect = ({
  value,
  onChange,
  disabled,
}: PhotoFormatSelectProps) => {
  return (
    <select
      disabled={disabled}
      value={value}
      onChange={onChange}
      className="select select-bordered"
    >
      {Object.keys(PhotoFormat).map((format) => (
        <option key={format} value={format}>
          {format}
        </option>
      ))}
    </select>
  );
};

export default PhotoFormatSelect;
