import { useContext } from "react";
import { UserDetailsContext } from "../context/UserDetailsContext";

export const useUserDetailsContext = () => {
  const context = useContext(UserDetailsContext);
  if (!context) {
    throw new Error(
      "useUserDetailsContext must be used within an UserDetailsProvider"
    );
  }
  return context;
};
