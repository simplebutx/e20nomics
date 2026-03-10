import { useState } from "react";
import api from "../../api"
import toast from "react-hot-toast";
import { useNavigate } from "react-router-dom";

export default function SignupPage() {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [userName, setUserName] = useState("");
    const [displayName, setDisplayName] = useState("");
    const nav = useNavigate();
  
    async function signup(e) {
        e.preventDefault();
        if (!email.trim() || !password.trim() || !userName.trim() || !displayName.trim()) {
           toast.error("모든 항목을 입력해 주세요.");
            return;
        }
        const form = {email, password, userName, displayName};
        try {
          await api.post("/api/auth/signup", form);
          toast.success("회원가입을 성공하였습니다.");
          nav("/login");
        } catch (err) {
          const message = err?.response?.data?.message || "회원가입 실패";
          toast.error(message);
        }
    }

    return (
         <>
            <form onSubmit={signup}>
                <input placeholder="이메일" value={email} onChange={(e) => setEmail(e.target.value)}></input>
                <input placeholder="비밀번호" value={password} onChange={(e) => setPassword(e.target.value)} type="password"></input>
                <input placeholder="이름" value={userName} onChange={(e) => setUserName(e.target.value)}></input>
                <input placeholder="닉네임" value={displayName} onChange={(e) => setDisplayName(e.target.value)}></input>
                <button type="submit">회원가입</button>
            </form>
        </>
    )
}