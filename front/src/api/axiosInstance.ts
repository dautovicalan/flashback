import axios from "axios";

// SINGLETON PATTERN
const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_BACKEND_API_URL,
});

export default axiosInstance;
