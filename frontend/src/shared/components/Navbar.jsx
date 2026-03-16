import { Link, useLocation } from "react-router-dom";
import "@/shared/css/Navbar.css";

export default function Navbar() {
  useLocation(); // 경로 바뀔 때 이 컴포넌트 다시 렌더되게
  const role = localStorage.getItem("role");

  return (
    <nav className="navbar">
      <div className="navbar-menu">
        {role === "ADMIN" ? (
          <>
            <Link to="/admin" className="nav-btn">관리자페이지</Link>
            <Link to="/adminSummarize" className="nav-btn">오늘의 뉴스 요약</Link>
            <Link to="/admin/terms" className="nav-btn">관리자 단어장</Link>
          </>
        ) : (
          <>
            <Link to="/today" className="nav-btn">오늘의 뉴스</Link>
            <Link to="/summarize" className="nav-btn">기사 요약</Link>
            <Link to="/summaries" className="nav-btn">내 기사</Link>
            <Link to="/terms" className="nav-btn">내 단어장</Link>
            <Link to="/mypage" className="nav-btn">마이페이지</Link>
          </>
        )}
      </div>
    </nav>
  );
}