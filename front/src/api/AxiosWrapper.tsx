import { useKeycloak } from "@react-keycloak/web";
import React, { useEffect } from "react";
import axiosInstance from "./axiosInstance";
import axios from "axios";

const AxiosWrapper = ({ children }: React.PropsWithChildren) => {
  const { keycloak } = useKeycloak();

  useEffect(() => {
    const requestInterceptor = axiosInstance.interceptors.request.use(
      (request) => {
        if (keycloak.authenticated) {
          request.headers["Authorization"] = `Bearer ${keycloak.idToken}`;
        }
        request.headers["Access-Control-Allow-Origin"] = "Content-Disposition";
        return request;
      }
    );

    const responseInterceptor = axiosInstance.interceptors.response.use(
      (response) => {
        return response;
      },
      (error) => {
        let errorMessage;
        if (axios.isAxiosError(error) && error.response) {
          errorMessage = error.response.data.message || "An error occurred";
        } else {
          errorMessage = "An error occurred";
        }
        return Promise.reject({
          ...error,
          message: errorMessage,
        });
      }
    );

    return () => {
      axiosInstance.interceptors.request.eject(requestInterceptor);
      axiosInstance.interceptors.response.eject(responseInterceptor);
    };
  }, [keycloak]);

  return <>{children}</>;
};

export default AxiosWrapper;
