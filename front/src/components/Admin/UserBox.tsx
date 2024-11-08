import { KeycloakUser } from "../../types/types";
import { Link } from "react-router-dom";

type UserBoxProps = {
  user: KeycloakUser;
};

const UserBox = ({ user }: UserBoxProps) => {
  return (
    <Link
      to={`/admin/user/${user.id}`}
      className="hover:scale-105 transform transition duration-300 ease-in-out"
    >
      <div className="hover:cursor-pointer">
        <div className="card shadow-lg bg-base-200 p-2">
          <figure className="w-full">
            <img
              src={`https://api.multiavatar.com/${user.username}.svg`}
              alt="User avatar"
              width={100}
              height={100}
            />
          </figure>
          <div className="card-body">
            <h2 className="card-title">
              Name: {`${user.firstName} ${user.lastName}`}
            </h2>
            <p>
              ID: <span>{user.id}</span>
            </p>
            <p>
              Usename: <span>{user.username}</span>
            </p>
          </div>
        </div>
      </div>
    </Link>
  );
};

export default UserBox;
