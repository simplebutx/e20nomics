import { Link, useLocation } from "react-router-dom";
import "@/shared/css/Navbar.css";

export default function Navbar() {
  const location = useLocation();
  const role = localStorage.getItem("role");

  function isActive(path) {
    return location.pathname === path;
  }

  function getNavClass(path) {
    return isActive(path) ? "nav-btn active" : "nav-btn";
  }

  return (
    <nav className="navbar">
      <div className="navbar-menu">
        {role === "ADMIN" ? (
          <>
            <Link to="/admin" className={getNavClass("/admin")}>
              관리자페이지
            </Link>
            <Link to="/adminSummarize" className={getNavClass("/adminSummarize")}>
              오늘의 뉴스 요약
            </Link>
            <Link to="/admin/terms" className={getNavClass("/admin/terms")}>
              관리자 단어장
            </Link>
          </>
        ) : (
          <>
            <Link to="/today" className={getNavClass("/today")}>
              오늘의 뉴스
            </Link>
            <Link to="/summarize" className={getNavClass("/summarize")}>
              기사 요약
            </Link>
            <Link to="/summaries" className={getNavClass("/summaries")}>
              내 기사
            </Link>
            <Link to="/terms" className={getNavClass("/terms")}>
              내 단어장
            </Link>
            <Link to="/mypage" className={getNavClass("/mypage")}>
              마이페이지
            </Link>
          </>
        )}
      </div>
    </nav>
  );
}