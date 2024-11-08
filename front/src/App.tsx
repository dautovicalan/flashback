import PhotoBox from "./components/PhotoBox";
import { PhotoUploadRequest } from "./types/types";
import LoadingScreen from "./components/LoadingScreen";
import { useState } from "react";
import { useAuthContext } from "./hooks/useAuthContext";
import { toast } from "react-toastify";
import UploadPhotoModal from "./components/UploadPhotoModal";
import Pagination from "./components/Pagination";
import { ErrorScreen } from "./components/ErrorBoundary";
import { useGetPhotos, useUploadPhoto } from "./hooks/api/photoApiHooks";
import { useDebounce } from "use-debounce";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { Dayjs } from "dayjs";
import { useUserDetailsContext } from "./hooks/useUserDetailsContext";

const SIZE = 10;

function App() {
  const { user, isAuthenticated } = useAuthContext();
  const { userDetails } = useUserDetailsContext();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [page, setPage] = useState(0);
  const [searchTerm, setSearchTerm] = useState("");
  const [fromDate, setFromDate] = useState<Dayjs | null>(null);
  const [toDate, setToDate] = useState<Dayjs | null>(null);
  const [debouncedSearchTerm] = useDebounce(searchTerm, 500);

  const { data, isLoading, isError, refetch } = useGetPhotos(
    page,
    SIZE,
    debouncedSearchTerm,
    fromDate,
    toDate
  );
  const uploadPhotoMutation = useUploadPhoto();

  const handleSubmit = async (uploadRequest: PhotoUploadRequest) => {
    uploadPhotoMutation.mutate(uploadRequest, {
      onSuccess: () => {
        toast.success("Photo uploaded successfully!");
        refetch();
      },
      onError: (error) => {
        toast.error(error.message);
      },
      onSettled: () => {
        setIsModalOpen(false);
      },
    });
  };

  const resetDates = () => {
    setFromDate(null);
    setToDate(null);
  };

  const isEligibleForUpload =
    isAuthenticated && user && userDetails.isProfileCompleted;

  return (
    <>
      {isLoading && <LoadingScreen />}
      {isError && <ErrorScreen />}
      {data && (
        <div className="w-full">
          <div className="flex gap-5 mt-5">
            <div className="w-full flex flex-col gap-5">
              <input
                value={searchTerm}
                type="text"
                className="input input-primary w-full"
                placeholder="Search photos by user, tag, description..."
                onChange={(e) => setSearchTerm(e.target.value)}
              />

              <div className="flex gap-4 justify-center items-center bg-white rounded-lg w-fit p-3">
                <DatePicker
                  label="From"
                  value={fromDate}
                  onChange={(date) => setFromDate(date)}
                />
                <DatePicker
                  label="To"
                  value={toDate}
                  onChange={(date) => setToDate(date)}
                />

                {(fromDate || toDate) && (
                  <button
                    className="btn btn-primary"
                    onClick={() => resetDates()}
                  >
                    X
                  </button>
                )}
              </div>
            </div>
            {isEligibleForUpload && (
              <button
                className="btn btn-primary"
                onClick={() => setIsModalOpen(true)}
              >
                Upload +
              </button>
            )}
          </div>

          <div className="flex gap-5 flex-wrap p-10">
            {data.content.map((photo, index) => (
              <PhotoBox key={index} photo={photo} />
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

          {isModalOpen && (
            <UploadPhotoModal
              onClose={() => setIsModalOpen(false)}
              onSubmit={handleSubmit}
            />
          )}
        </div>
      )}
    </>
  );
}

export default App;
