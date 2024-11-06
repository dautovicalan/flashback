import { Navigate, Outlet, RouteProps } from "react-router-dom";
import { useAuthContext } from "../hooks/useAuthContext";

const UnauthenticatedRoute: React.FC<RouteProps> = () => {
  const { isAuthenticated } = useAuthContext();

  if (isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  return <Outlet />;
};

export default UnauthenticatedRoute;
