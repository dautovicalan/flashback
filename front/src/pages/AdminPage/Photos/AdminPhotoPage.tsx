import { useState } from "react";
import { useGetPhotosAsAdmin } from "../../../hooks/api/adminApiHooks";
import LoadingScreen from "../../../components/LoadingScreen";
import { ErrorScreen } from "../../../components/ErrorBoundary";
import Pagination from "../../../components/Pagination";
import PhotoBox from "../../../components/PhotoBox";
import { useDebounce } from "use-debounce";

const SIZE = 10;

const AdminPhotoPage = () => {
  const [page, setPage] = useState(0);
  const [searchTerm, setSearchTerm] = useState("");
  const [debouncedSearchTerm] = useDebounce(searchTerm, 1000);

  const { data, isLoading, error } = useGetPhotosAsAdmin(
    page,
    SIZE,
    debouncedSearchTerm
  );

  return (
    <>
      {isLoading && <LoadingScreen />}
      {error && <ErrorScreen message={error.message} />}
      {data && (
        <div className="w-full">
          <div className="flex gap-5 mt-5">
            <input
              value={searchTerm}
              type="text"
              className="input input-primary w-full"
              placeholder="Search photos by user, tag, description..."
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>

          <div className="flex gap-5 flex-wrap p-10">
            {data.content.map((photo, index) => (
              <PhotoBox key={index} photo={photo} adminView />
            ))}
          </div>

          {data.content.length === 0 && (
            <div className="flex justify-center items-center h-96">
              <p className="text-lg font-semibold text-primary">
                No photos found! 😢
              </p>
            </div>
          )}

          <Pagination
            page={page}
            setPage={(page) => setPage(page)}
            totalPage={data.totalPages}
          />
        </div>
      )}
    </>
  );
};

export default AdminPhotoPage;
