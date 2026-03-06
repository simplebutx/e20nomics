import { Link } from "react-router-dom";
import "./Navbar.css";

export default function Navbar() {
  return (
    <nav className="navbar">
      <div className="navbar-menu">
        <Link to="/today" className="nav-btn">오늘의 뉴스</Link>
        <Link to="/feed" className="nav-btn">피드</Link>
        <Link to="/summarize" className="nav-btn">기사 요약</Link>
        <Link to="/mypage" className="nav-btn">마이페이지</Link>
      </div>
    </nav>
  );
}