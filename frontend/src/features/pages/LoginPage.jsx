import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../../api"
import toast from "react-hot-toast";

export default function LoginPage() {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const nav = useNavigate();

    async function login(e) {
        e.preventDefault();
        if (!email.trim() || !password.trim()) {
           toast.error("모든 항목을 입력해 주세요.");
           return;
        }
        const form = {email, password};
        try {
          const res = await api.post("/api/auth/login", form);
          localStorage.setItem("accessToken", res.data.accessToken);
          toast.success("로그인을 성공하였습니다.");
          nav("/mypage");
        } catch (err) {
          const message = err?.response?.data?.message || "로그인 실패";
          toast.error(message);
        }
    }

    return (
        <>
            <form onSubmit={login}>
                <input placeholder="이메일" value={email} onChange={(e) => setEmail(e.target.value)}></input>
                <input placeholder="비밀번호" value={password} onChange={(e) => setPassword(e.target.value)} type="password"></input>
                <button type="submit">로그인</button>
            </form>
            <button>구글 로그인</button>
            <Link to="/signup" className="nav-btn">회원가입</Link>
        </>
    )
}