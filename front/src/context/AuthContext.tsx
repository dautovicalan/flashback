import { createContext, useEffect, useState } from "react";
import { Role, KeycloakUser } from "../types/types";
import { useKeycloak } from "@react-keycloak/web";

type AuthContextType = {
  user: KeycloakUser | null;
  isAuthenticated: boolean;
  login: () => void;
  logout: () => void;
  register: () => void;
  getToken: () => string | undefined;
  getUsername: () => string | undefined;
  accountManagement: () => void;
};

export const AuthContext = createContext<AuthContextType | undefined>(
  undefined
);

type AuthProviderProps = {
  children: React.ReactNode;
};

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const { keycloak } = useKeycloak();
  const [user, setUser] = useState<KeycloakUser | null>(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const checkAuth = async () => {
      if (keycloak.authenticated) {
        const { email, given_name, family_name, preferred_username } =
          keycloak.tokenParsed!;
        const role = keycloak.hasRealmRole("ADMIN") ? Role.ADMIN : Role.USER;

        setUser({
          id: keycloak.subject!,
          username: preferred_username,
          email: email as string,
          firstName: given_name || "",
          lastName: family_name || "",
          role,
        });
        setIsAuthenticated(true);
      }
    };
    checkAuth();
  }, [keycloak, keycloak.authenticated]);

  const login = () => {
    keycloak.login();
  };

  const logout = () => {
    keycloak.logout();
  };

  const register = () => {
    keycloak.register();
  };

  const accountManagement = () => {
    keycloak.accountManagement();
  };

  const getToken = () => {
    return keycloak.token;
  };

  const getUsername = () => {
    return user?.username;
  };

  const value = {
    user,
    isAuthenticated,
    login,
    logout,
    register,
    accountManagement,
    getToken,
    getUsername,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
