import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function OAuthSuccessPage() {
  const nav = useNavigate();

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get("token");

    if (token) {
      localStorage.setItem("accessToken", token);
      nav("/mypage", { replace: true });
    } else {
      nav("/login", { replace: true });
    }
  }, [nav]);

  return <div>로그인 처리 중...</div>;
}