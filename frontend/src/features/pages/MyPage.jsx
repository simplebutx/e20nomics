import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api"
import toast from "react-hot-toast";
import MySummaries from "../components/MySummaries";
import MyTerms from "../components/MyTerms";

export default function MyPage() {
    const nav = useNavigate();

    const [user, setUser] = useState(null);
    const [openMySummaries, setOpenMySummaries] = useState(false);
    const [openMyTerms, setOpenMyTerms] = useState(false);

    async function myPage(e) {
        try {
            const res = await api.get("/api/me");
            setUser(res.data);
        } catch {
            localStorage.removeItem("accessToken");
            nav("/login");
        }
    }

    useEffect(() => {
        const token = localStorage.getItem("accessToken");
        if(!token) nav("/login");
        else myPage();
    }, []);

    function logout() {
        localStorage.removeItem("accessToken");
        toast.success("로그아웃 되었습니다");
        nav("/today");
    }

    if (!user) {
        return <div>로딩중...</div>;
    }

    return (
        <>
            <h1>마이페이지</h1>
            <button onClick={logout}>로그아웃</button>
            <p>내이메일: {user.email}</p>
            <p>내이름: {user.userName}</p>
            <p>내별명: {user.displayName}</p>
            <p>내 선호도 보기 버튼</p>
            <p onClick={()=>setOpenMySummaries(prev => !prev)}>내가 저장한 기사</p>
            {openMySummaries ? <MySummaries /> : null}
            
            <p onClick={()=>setOpenMyTerms(prev => !prev)}>내 단어 사전</p>
            {openMyTerms ? <MyTerms /> : null}

            <button>회원탈퇴</button>
        </>
    )
}