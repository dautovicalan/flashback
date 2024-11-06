import { Outlet } from "react-router-dom";
import Navbar from "../layout/Navbar";
import { ToastContainer } from "react-toastify";
import AdminSidebar from "./AdminSidebar";

const AdminWrapper = () => {
  return (
    <>
      <Navbar />
      <main className="flex flex-row">
        <AdminSidebar />
        <div className="w-full h-full p-10">
          <Outlet />
        </div>
      </main>
      <ToastContainer position="bottom-right" autoClose={3000} theme="dark" />
    </>
  );
};

export default AdminWrapper;
