import { useState } from "react";
import { Role, SubscriptionPlan, UpdateUserData } from "../../types/types";

type ModifyProfileModalProps = {
  user: {
    firstName: string;
    lastName: string;
    subscriptionPlan: SubscriptionPlan;
    role: Role;
  };
  onClose: () => void;
  onSubmit: (newData: UpdateUserData) => void;
};

const ModifyProfileModal = ({
  onClose,
  onSubmit,
  user,
}: ModifyProfileModalProps) => {
  const [updateData, setUpdateData] = useState<UpdateUserData>({
    firstName: user.firstName,
    lastName: user.lastName,
    subscriptionPlan: user.subscriptionPlan,
    resetDailyUploadCount: false,
  });

  const handleOnChange = (field: string, value: string) => {
    setUpdateData({ ...updateData, [field]: value });
  };

  const handleCheckboxChange = (field: string, value: boolean) => {
    setUpdateData({ ...updateData, [field]: value });
  };

  const handleSubmit = () => {
    onSubmit(updateData);
  };

  return (
    <div className="modal modal-open">
      <div className="modal-box">
        <h2 className="font-bold text-lg mb-4">Change User Info</h2>

        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">First Name:</span>
          </label>
          <input
            type="text"
            className="input input-bordered"
            value={updateData.firstName}
            onChange={(e) => handleOnChange("firstName", e.target.value)}
          />
        </div>

        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Last Name:</span>
          </label>
          <input
            type="text"
            className="input input-bordered"
            value={updateData.lastName}
            onChange={(e) => handleOnChange("lastName", e.target.value)}
          />
        </div>

        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Current plan:</span>
          </label>
          <select
            value={updateData.subscriptionPlan}
            className="select select-bordered"
            onChange={(e) => handleOnChange("subscriptionPlan", e.target.value)}
          >
            <option value="FREE">FREE</option>
            <option value="PRO">PRO</option>
            <option value="GOLD">GOLD</option>
          </select>
        </div>

        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Reset daily upload:</span>
          </label>
          <input
            type="checkbox"
            className="checkbox"
            checked={updateData.resetDailyUploadCount}
            onChange={(e) =>
              handleCheckboxChange("resetDailyUploadCount", e.target.checked)
            }
          />
        </div>

        {/* Modal Actions */}
        <div className="modal-action">
          <button className="btn btn-primary" onClick={handleSubmit}>
            Modify Profile
          </button>
          <button className="btn" onClick={onClose}>
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
};

export default ModifyProfileModal;
