import { Outlet } from "react-router-dom";
import Navbar from "./Navbar";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useKeycloak } from "@react-keycloak/web";
import LoadingScreen from "../components/LoadingScreen";

const LayoutWrapper = () => {
  const { initialized } = useKeycloak();

  if (!initialized) {
    return <LoadingScreen />;
  }

  return (
    <>
      <Navbar />
      <main className="container mx-auto">
        <Outlet />
        <ToastContainer position="bottom-right" autoClose={3000} theme="dark" />
      </main>
    </>
  );
};

export default LayoutWrapper;
