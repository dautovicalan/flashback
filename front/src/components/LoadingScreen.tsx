import SpinnerIcon from "../icons/SpinnerIcon";

const LoadingScreen = () => {
  return (
    <div className="flex justify-center items-center min-h-screen">
      <div className="flex flex-col items-center">
        <SpinnerIcon />
        <p className="mt-4 text-lg font-semibold text-primary">Loading...</p>
      </div>
    </div>
  );
};

export default LoadingScreen;
