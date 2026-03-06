import { Link } from "react-router-dom";

export default function LoginPage() {

    return (
        <>
            <form>
                <input placeholder="아이디"></input>
                <input placeholder="비밀번호"></input>
                <button>로그인</button>
            </form>
            <button>구글 로그인</button>
            <Link to="/signup" className="nav-btn">회원가입</Link>
        </>
    )
}