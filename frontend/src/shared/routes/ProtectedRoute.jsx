import { useEffect } from "react";
import { Navigate, Outlet, useLocation } from "react-router-dom";
import toast from "react-hot-toast";

export default function ProtectedRoute({ requireRole }) {
  const token = localStorage.getItem("accessToken");
  const role = localStorage.getItem("role");
  const location = useLocation();

  useEffect(() => {
    if (!token) {
      toast.error("로그인 후 이용 가능합니다.");
      return;
    }

    if (requireRole && role !== requireRole) {
      toast.error("접근 권한이 없습니다.");
    }
  }, [token, role, requireRole]);

  if (!token) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }

  if (requireRole && role !== requireRole) {
    return <Navigate to="/today" replace />;
  }

  return <Outlet />;
}