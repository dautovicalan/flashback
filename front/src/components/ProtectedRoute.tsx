import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import { Role } from "../types/types";
import { useAuthContext } from "../hooks/useAuthContext";

type ProtectedRouteProps = {
  role?: Role;
};

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({
  role,
}: ProtectedRouteProps) => {
  const { isAuthenticated, user } = useAuthContext();

  if (!isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  if (role && user?.role !== role) {
    return <Navigate to="/404" replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
