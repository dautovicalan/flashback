import React, { useState } from "react";
import { useCompleteProfile } from "../../hooks/api/userProfileApiHooks";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { CompleteProfile, SubscriptionPlan } from "../../types/types";

const CompleteProfilePage = () => {
  const navigate = useNavigate();
  const completeProfileMutation = useCompleteProfile();

  const [userInput, setUserInput] = useState<CompleteProfile>({
    subscriptionPlan: SubscriptionPlan.FREE,
  });

  const handleOnChange = (field: string, value: string) => {
    setUserInput({ ...userInput, [field]: value });
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    completeProfileMutation.mutate(userInput, {
      onSuccess: () => {
        navigate("/");
        toast.success("Profile completed successfully!");
      },
      onError: (error) => {
        toast.error(error.message);
      },
    });
  };

  return (
    <div className="flex justify-center items-center min-h-screen">
      <div className="card w-full max-w-md shadow-xl bg-base-100 p-6">
        <h2 className="text-center text-2xl font-bold mb-6">
          Select Subscription Plan
        </h2>
        <form onSubmit={handleSubmit}>
          <div className="form-control mb-4">
            <label className="label">
              <span className="label-text">Subscription plan</span>
            </label>
            <select
              className="select select-bordered"
              value={userInput.subscriptionPlan}
              onChange={(e) =>
                handleOnChange("subscriptionPlan", e.target.value)
              }
            >
              <option value={SubscriptionPlan.FREE}>Free</option>
              <option value={SubscriptionPlan.PRO}>Pro</option>
              <option value={SubscriptionPlan.GOLD}>Gold</option>
            </select>
          </div>

          <div className="form-control mt-6">
            <input
              className="btn btn-primary"
              type="submit"
              placeholder="Complete profile"
            />
          </div>
        </form>
      </div>
    </div>
  );
};

export default CompleteProfilePage;
