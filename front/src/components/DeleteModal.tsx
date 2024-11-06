import React from "react";

type DeleteModalProps = {
  text: string;
  onClose: () => void;
  onSubmit: () => void;
};

const DeleteModal: React.FC<DeleteModalProps> = ({
  text,
  onClose,
  onSubmit,
}) => {
  return (
    <div className="modal modal-open">
      <div className="modal-box">
        <h2 className="font-bold text-lg mb-4">{text}</h2>

        <div className="modal-action">
          <button className="btn btn-primary" onClick={onSubmit}>
            Yes
          </button>
          <button className="btn btn-secondary" onClick={onClose}>
            No
          </button>
        </div>
      </div>
    </div>
  );
};

export default DeleteModal;
