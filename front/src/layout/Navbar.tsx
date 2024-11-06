import { Link } from "react-router-dom";
import { Role } from "../types/types";
import { useUserDetailsContext } from "../hooks/useUserDetailsContext";
import { useAuthContext } from "../hooks/useAuthContext";

const Navbar = () => {
  const { user, login, register, logout } = useAuthContext();
  const { userDetails } = useUserDetailsContext();

  return (
    <nav className="navbar bg-base-100 rounded-full">
      <div className="flex-1">
        <Link to="/" className="text-lg font-bold">
          Flashback
        </Link>
        <Link to="/" className="ml-5">
          Photos
        </Link>
        {user?.role === Role.ADMIN && (
          <Link to="/admin" className="ml-5">
            Admin
          </Link>
        )}
      </div>
      {user ? (
        <div className="flex-none">
          {!userDetails.isProfileCompleted ? (
            <Link to="/complete-profile" className="btn btn-warning">
              Action required: Complete profile
            </Link>
          ) : (
            <p className="p-3 font-bold">
              Welcome, {user.firstName} {user.lastName}
            </p>
          )}
          <div className="dropdown dropdown-end">
            <div
              tabIndex={0}
              role="button"
              className="btn btn-ghost btn-circle avatar"
            >
              <div className="w-10 rounded-full">
                <img
                  alt="avatar"
                  src={`https://api.multiavatar.com/${user.username}.svg`}
                />
              </div>
            </div>
            <ul
              tabIndex={0}
              className="menu menu-sm dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-52 p-2 shadow"
            >
              <li>
                <Link to={"/profile"}>Profile</Link>
              </li>
              <li>
                <a className="text-red-500" onClick={() => logout()}>
                  Logout
                </a>
              </li>
            </ul>
          </div>
        </div>
      ) : (
        <div className="flex-none flex gap-5">
          <button className="btn btn-secondary" onClick={() => register()}>
            Register
          </button>
          <button className="btn btn-primary" onClick={() => login()}>
            Login
          </button>
        </div>
      )}
    </nav>
  );
};

export default Navbar;
