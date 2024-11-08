import React from "react";

type DeleteModalProps = {
  text: string;
  onClose: () => void;
  onSubmit: () => void;
  loading?: boolean;
};

const DeleteModal: React.FC<DeleteModalProps> = ({
  text,
  onClose,
  onSubmit,
  loading,
}) => {
  return (
    <div className="modal modal-open">
      <div className="modal-box">
        <h2 className="font-bold text-lg mb-4">{text}</h2>

        <div className="modal-action">
          <button
            className="btn btn-primary"
            onClick={onSubmit}
            disabled={loading}
          >
            Yes
          </button>
          <button className="btn" onClick={onClose} disabled={loading}>
            No
          </button>
        </div>
      </div>
    </div>
  );
};

export default DeleteModal;
