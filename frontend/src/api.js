import axios from "axios";
import toast from "react-hot-toast";

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8080",
    headers: {
         "Content-Type": "application/json",
    },
    withCredentials: false,
});


// 요청 인터셉터: JWT 토큰 자동 첨부
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 응답 인터셉터
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error?.response?.status;
    const data = error?.response?.data;

    if(status === 500) {   // 500 공통 처리
        toast.error("서버 오류가 발생했습니다.");
        console.log("서버에러")
    }

     return Promise.reject(error);    // 나머지 에러는 그대로 던져서 각 화면에서 커스텀 처리
  }
);

export default api;