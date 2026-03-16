import { useState } from "react";
import api from "../../api"
import toast from "react-hot-toast";
import { useNavigate, Link } from "react-router-dom";
import "@/shared/css/SignupPage.css";

export default function SignupPage() {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [userName, setUserName] = useState("");
    const [loading, setLoading] = useState(false);
    const nav = useNavigate();
  
    async function signup(e) {
        e.preventDefault();
        if (!email.trim() || !password.trim() || !userName.trim() || !displayName.trim()) {
           toast.error("모든 항목을 입력해 주세요.");
            return;
        }
        const form = {email, password, userName};
        try {
          setLoading(true);
          await api.post("/api/auth/signup", form);
          toast.success("회원가입을 성공하였습니다.");
          nav("/login");
        } catch (err) {
          const message = err?.response?.data?.message || "회원가입 실패";
          toast.error(message);
        } finally {
            setLoading(false);
        }
    }

     return (
    <div className="signup-page">
      <div className="signup-container">
        <div className="signup-card">
          <div className="signup-header">
            <p className="signup-label">Create Account</p>
            <h1>회원가입</h1>
            <p className="signup-desc">
              경제 뉴스 요약과 나만의 단어장을 시작해보세요.
            </p>
          </div>

          <form className="signup-form" onSubmit={signup}>
            <div className="form-group">
              <label>이메일</label>
              <input type="email" placeholder="example@email.com" value={email} onChange={(e) => setEmail(e.target.value)}/>
            </div>

            <div className="form-group">
              <label>비밀번호</label>
              <input type="password" placeholder="비밀번호를 입력하세요" value={password} onChange={(e) => setPassword(e.target.value)}/>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>이름</label>
                <input type="text" placeholder="이름" value={userName} onChange={(e) => setUserName(e.target.value)}/>
              </div>
            </div>

            <button className="signup-btn" type="submit" disabled={loading}>
              {loading ? "가입 중..." : "회원가입"}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}