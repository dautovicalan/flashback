type PaginationProps = {
  page: number;
  setPage: (page: number) => void;
  totalPage: number;
};

const Pagination = ({ page, setPage, totalPage }: PaginationProps) => {
  const handleSetPage = (page: number) => {
    if (page < 0 || page > totalPage - 1) {
      return;
    }
    setPage(page);
  };

  return (
    <div className="flex justify-center items-center gap-5 mt-5">
      <button
        onClick={() => handleSetPage(page - 1)}
        disabled={page === 0}
        className="btn btn-primary"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          className="h-6 w-6"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth={2}
            d="M15 19l-7-7 7-7"
          />
        </svg>
      </button>
      <span>{page}</span>
      <button
        onClick={() => handleSetPage(page + 1)}
        disabled={page === totalPage - 1}
        className="btn btn-primary"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          className="h-6 w-6"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth={2}
            d="M9 5l7 7-7 7"
          />
        </svg>
      </button>
    </div>
  );
};

export default Pagination;
