import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import api from "../../api";
import toast from "react-hot-toast";
import handleApiError from "@/shared/utils/handleApiError";
import "@/shared/css/LoginPage.css";
import "@/shared/css/Button.css";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const nav = useNavigate();
  const location = useLocation();

  async function login(e) {
    e.preventDefault();

    if (!email.trim() || !password.trim()) {
      toast.error("모든 항목을 입력해 주세요.");
      return;
    }

    const form = { email, password };

    try {
      setLoading(true);
      const res = await api.post("/api/auth/login", form);

      localStorage.setItem("accessToken", res.data.accessToken);
      localStorage.setItem("role", res.data.role);

      toast.success("로그인에 성공했습니다.");

      const redirectTo = location.state?.from?.pathname;

      if (redirectTo) {
        nav(redirectTo, { replace: true });
        return;
      }

      nav(res.data.role === "ADMIN" ? "/admin" : "/mypage", { replace: true });
    } catch (e) {
      handleApiError(e, "로그인 실패");
    } finally {
      setLoading(false);
    }
  }

  function googleLogin() {
    try {
      const backendUrl = import.meta.env.VITE_API_BASE_URL;

      if(!backendUrl) {
        toast.error("구글로그인불가");
        return;
      }
      
      const url = `${backendUrl.replace(/\/$/, "")}/oauth2/authorization/google`;
      window.location.assign(url);
    } catch(e) {
      handleApiError(e, "구글 로그인 실패");
    }
  }

  return (
    <div className="login-page">
      <div className="login-container">
        <div className="login-card">
          <div className="login-header">
            <p className="login-label">Welcome Back</p>
            <h1>로그인</h1>
            <p className="login-desc">
               임시 유저 ID: aa / PW: aa <br />
               관리자 ID: admin / PW: 123
            </p>
          </div>

          <form className="login-form" onSubmit={login}>
            <div className="login-form-group">
              <label>이메일</label>
              <input
                placeholder="example@email.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>

            <div className="login-form-group">
              <label>비밀번호</label>
              <input
                type="password"
                placeholder="비밀번호를 입력하세요"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>

            <button
              className="btn btn-primary btn-block"
              type="submit"
              disabled={loading}
            >
              {loading ? "로그인 중..." : "로그인"}
            </button>
          </form>

          <div className="login-divider">
            <span>또는</span>
          </div>

          <button
            className="btn btn-outline btn-block"
            type="button"
            onClick={googleLogin}
          >
            구글 로그인
          </button>

          <div className="login-footer">
            <span>아직 계정이 없나요?</span>
            <Link to="/signup">회원가입</Link>
          </div>
        </div>
      </div>
    </div>
  );
}
