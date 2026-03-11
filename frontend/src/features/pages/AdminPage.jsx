import AdminUserList from "../components/AdminUserList";
import AdminTermList from "../components/AdminTermList";
import { Link, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import "@/features/css/AdminPage.css";

export default function AdminPage() {
  const nav = useNavigate();

  function logout() {
    localStorage.removeItem("accessToken");
    toast.success("로그아웃 되었습니다.");
    nav("/today");
  }

  return (
    <div className="admin-page">
      <div className="admin-container">
        <header className="admin-header-card">
          <div className="admin-header-text">
            <p className="admin-label">Admin Console</p>
            <h1>관리자 페이지</h1>
            <p className="admin-desc">
              오늘의 뉴스, 공용 사전, 회원 정보를 한 곳에서 관리합니다.
            </p>
          </div>

          <div className="admin-header-actions">
            <Link to="/adminSummarize" className="admin-primary-link">
              오늘의 뉴스 등록
            </Link>
            <button onClick={logout} className="admin-logout-btn" type="button">
              관리자 로그아웃
            </button>
          </div>
        </header>

        <section className="admin-section">
          <div className="admin-section-top">
            <h2>공용 사전 관리</h2>
            <p>전체 사용자에게 보여줄 경제 용어를 관리합니다.</p>
          </div>
          <div className="admin-section-card">
            <AdminTermList />
          </div>
        </section>

        <section className="admin-section">
          <div className="admin-section-top">
            <h2>오늘의 뉴스 관리</h2>
            <p>등록된 오늘의 뉴스 목록은 추후 이 영역에서 관리하면 됩니다.</p>
          </div>
          <div className="admin-section-card admin-empty-card">
            <p>뉴스 목록 기능을 추가하면 여기에 연결하면 됩니다.</p>
          </div>
        </section>

                <section className="admin-section">
          <div className="admin-section-top">
            <h2>회원 관리</h2>
            <p>가입한 회원 목록과 계정 정보를 확인합니다.</p>
          </div>
          <div className="admin-section-card">
            <AdminUserList />
          </div>
        </section>
      </div>
    </div>
  );
}