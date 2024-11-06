import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import { useUserDetailsContext } from "../hooks/useUserDetailsContext";

const CompleteProfileRoute: React.FC = () => {
  const { userDetails } = useUserDetailsContext();

  if (userDetails.isProfileCompleted === true) {
    return <Navigate to="/" replace />;
  }

  return <Outlet />;
};

export default CompleteProfileRoute;
