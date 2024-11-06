import { createRoot } from "react-dom/client";
import App from "./App.tsx";
import "./index.css";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import LayoutWrapper from "./layout/LayoutWrapper.tsx";
import ProtectedRoute from "./components/ProtectedRoute.tsx";
import { AuthProvider } from "./context/AuthContext.tsx";
import ProfilePage from "./pages/ProfilePage/ProfilePage.tsx";
import PhotoPage from "./pages/PhotoPage/PhotoPage.tsx";
import NotFoundPage from "./pages/NotFoundPage/NotFoundPage.tsx";
import AdminPage from "./pages/AdminPage/AdminPage.tsx";
import { Role } from "./types/types.ts";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import CompleteProfilePage from "./pages/CompleteProfilePage/CompleteProfilePage.tsx";
import CompleteProfileRoute from "./components/CompleteProfileRoute.tsx";
import ErrorBoundary from "./components/ErrorBoundary.tsx";
import AdminWrapper from "./components/AdminWrapper.tsx";
import AdminUsersPage from "./pages/AdminPage/Users/AdminUsersPage.tsx";
import AdminUserInfoPage from "./pages/AdminPage/Users/AdminUserInfoPage.tsx";
import AdminPhotoPage from "./pages/AdminPage/Photos/AdminPhotoPage.tsx";
import AdminPhotoInformationPage from "./pages/AdminPage/Photos/AdminPhotoInformationPage.tsx";
import { AdminStatisticsPage } from "./pages/AdminPage/Statistics/AdminStatisticsPage.tsx";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { ReactKeycloakProvider } from "@react-keycloak/web";
import keycloak from "./keycloak.config.ts";
import AxiosWrapper from "./api/AxiosWrapper.tsx";
import { UserDetailsProvider } from "./context/UserDetailsContext.tsx";

const router = createBrowserRouter([
  {
    path: "/",
    element: <LayoutWrapper />,
    children: [
      {
        path: "/",
        element: <App />,
      },
      {
        path: "/photo/:id",
        element: <PhotoPage />,
      },
      {
        path: "/profile",
        element: <ProtectedRoute />,
        children: [
          {
            path: "",
            element: <ProfilePage />,
          },
        ],
      },
      {
        path: "/complete-profile",
        element: <CompleteProfileRoute />,
        children: [
          {
            path: "",
            element: <CompleteProfilePage />,
          },
        ],
      },
      {
        path: "*",
        element: <NotFoundPage />,
      },
    ],
  },
  {
    path: "/admin",
    element: <AdminWrapper />,
    children: [
      {
        path: "/admin",
        element: <ProtectedRoute role={Role.ADMIN} />,
        children: [
          {
            path: "",
            element: <AdminPage />,
          },
        ],
      },
      {
        path: "/admin/users",
        element: <ProtectedRoute role={Role.ADMIN} />,
        children: [
          {
            path: "",
            element: <AdminUsersPage />,
          },
        ],
      },
      {
        path: "/admin/user/:id",
        element: <ProtectedRoute role={Role.ADMIN} />,
        children: [
          {
            path: "",
            element: <AdminUserInfoPage />,
          },
        ],
      },
      {
        path: "/admin/statistic",
        element: <ProtectedRoute role={Role.ADMIN} />,
        children: [
          {
            path: "",
            element: <AdminStatisticsPage />,
          },
        ],
      },
      {
        path: "/admin/photos",
        element: <ProtectedRoute role={Role.ADMIN} />,
        children: [
          {
            path: "",
            element: <AdminPhotoPage />,
          },
        ],
      },
      {
        path: "/admin/photo/:id",
        element: <ProtectedRoute role={Role.ADMIN} />,
        children: [
          {
            path: "",
            element: <AdminPhotoInformationPage />,
          },
        ],
      },
      {
        path: "*",
        element: <NotFoundPage />,
      },
    ],
  },
]);

const queryClient = new QueryClient();

createRoot(document.getElementById("root")!).render(
  <ReactKeycloakProvider authClient={keycloak}>
    <ErrorBoundary>
      <AxiosWrapper>
        <QueryClientProvider client={queryClient}>
          <AuthProvider>
            <UserDetailsProvider>
              <LocalizationProvider dateAdapter={AdapterDayjs}>
                <RouterProvider router={router} />
              </LocalizationProvider>
            </UserDetailsProvider>
          </AuthProvider>
        </QueryClientProvider>
      </AxiosWrapper>
    </ErrorBoundary>
  </ReactKeycloakProvider>
);
