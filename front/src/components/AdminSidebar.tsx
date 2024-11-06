import { Link } from "react-router-dom";

const AdminSidebar = () => {
  return (
    <div className="flex h-screen w-1/6">
      {/* Sidebar */}
      <div className="w-64 bg-base-200 p-5 flex flex-col justify-start">
        {/* Logo or Title */}
        <h1 className="text-xl font-bold mb-10">Admin Panel</h1>

        {/* Navigation */}
        <ul className="menu p-2 flex flex-col justify-between">
          <li className="mb-2">
            <Link to="/admin/users" className="flex gap-2 items-center">
              <span className="icon">{/* Dashboard Icon */}</span>
              <span>Users</span>
            </Link>
          </li>
          <li className="mb-2">
            <Link to="/admin/statistic" className="flex gap-2 items-center">
              <span className="icon">{/* Profile Icon */}</span>
              <span>Statistics</span>
            </Link>
          </li>
          <li className="mb-2">
            <Link to="/admin/photos" className="flex gap-2 items-center">
              <span className="icon">{/* Settings Icon */}</span>
              <span>Photos</span>
            </Link>
          </li>
        </ul>
      </div>
    </div>
  );
};

export default AdminSidebar;
