import React, { useState } from "react";
import { SubscriptionPlan } from "../types/types";

type ChangePlanModalProps = {
  currentSubscriptionPlan: SubscriptionPlan;
  onClose: () => void;
  onSubmit: (newPlan: SubscriptionPlan) => void;
};

const ChangePlanModal: React.FC<ChangePlanModalProps> = ({
  currentSubscriptionPlan,
  onClose,
  onSubmit,
}) => {
  const [subscriptionPlan, setSubscriptionPlan] = useState(
    currentSubscriptionPlan
  );

  const handleChangeSubscriptionPlan = (newPlan: SubscriptionPlan) => {
    setSubscriptionPlan(newPlan);
  };

  const handleClick = () => {
    onSubmit(subscriptionPlan);
    onClose();
  };

  return (
    <div className="modal modal-open">
      <div className="modal-box">
        <h2 className="font-bold text-lg mb-4">Change Subscription Plan</h2>

        <div className="form-control mb-4">
          <label className="label">
            <span className="label-text">Current plan:</span>
          </label>
          <select
            value={subscriptionPlan}
            className="select select-bordered"
            onChange={(e) =>
              handleChangeSubscriptionPlan(e.target.value as SubscriptionPlan)
            }
          >
            <option value="FREE">FREE</option>
            <option value="PRO">PRO</option>
            <option value="GOLD">GOLD</option>
          </select>
        </div>

        {/* Modal Actions */}
        <div className="modal-action">
          <button className="btn btn-primary" onClick={handleClick}>
            Change Plan
          </button>
          <button className="btn" onClick={onClose}>
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
};

export default ChangePlanModal;
