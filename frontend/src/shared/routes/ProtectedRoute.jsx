import { Navigate, Outlet } from "react-router-dom";
import toast from "react-hot-toast";

export default function ProtectedRoute() {
  const token = localStorage.getItem("accessToken");

  // 토큰이 없으면 로그인 페이지로
  if (!token) {
    toast("로그인 후 이용 가능합니다.")
    return <Navigate to="/login" replace />;
  }

  // 토큰 있으면 자식 라우트 렌더
  return <Outlet />;
}