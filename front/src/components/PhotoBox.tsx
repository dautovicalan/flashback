import { Photo } from "../types/types";
import Tags from "./Tags";
import { toFormattedDate } from "../utils/dateUtils";
import { Link } from "react-router-dom";

type PhotoBoxProps = {
  photo: Photo;
  adminView?: boolean;
};

const PhotoBox = ({ photo, adminView = false }: PhotoBoxProps) => {
  return (
    <Link to={adminView ? `/admin/photo/${photo.id}` : `/photo/${photo.id}`}>
      <div className="hover:cursor-pointer">
        <div className="card shadow-lg bg-base-200">
          <figure className="w-48 h-48 rounded-lg overflow-hidden mx-auto p-2">
            <img
              src={photo.url}
              alt={photo.description}
              className="object-cover w-full h-full hover:opacity-80 cursor-pointer"
            />
          </figure>
          <div className="card-body">
            <h2 className="card-title">Description: {photo.description}</h2>
            <p>
              Author: <span>{photo.author}</span>
            </p>
            <p>
              Upoad date: <span>{toFormattedDate(photo.uploadDate)}</span>
            </p>
            <Tags tags={photo.tags} />
          </div>
        </div>
      </div>
    </Link>
  );
};

export default PhotoBox;
