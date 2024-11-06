const LoadingScreen = () => {
  return (
    <div className="flex justify-center items-center min-h-screen">
      <div className="flex flex-col items-center">
        <div className="w-12 h-12 border-4 border-dashed rounded-full animate-spin border-primary"></div>
        <p className="mt-4 text-lg font-semibold text-primary">Loading...</p>
      </div>
    </div>
  );
};

export default LoadingScreen;
