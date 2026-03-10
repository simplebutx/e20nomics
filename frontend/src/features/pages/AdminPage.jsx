import AdminUserList from "../components/AdminUserList"
import AdminTermList from "../components/AdminTermList"
import { Link, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";

export default function AdminPage() {
    const nav = useNavigate();

    function logout() {
        localStorage.removeItem("accessToken");
        toast.success("로그아웃 되었습니다");
        nav("/today");
    }

    return (
        <>
            <button onClick={logout}>관리자 로그아웃</button>
            <Link to="/adminSummarize" className="nav-btn">오늘의뉴스 등록하기 버튼</Link>
            <p>뉴스 목록</p>

            <div>사전 목록</div>
            <div><AdminTermList /></div>


            <div>회원 목록</div>
            <div><AdminUserList /></div>
        </>
    )
}