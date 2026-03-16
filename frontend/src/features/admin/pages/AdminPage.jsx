import AdminUserList from "./AdminUserList";
import { Link, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import "@/features/admin/css/AdminPage.css";
import AdminTodayNewsList from "./AdminTodayNewsList";

export default function AdminPage() {
  const nav = useNavigate();

  function logout() {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("role");
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
            <button onClick={logout} className="admin-logout-btn" type="button">
              로그아웃
            </button>
          </div>
        </header>

        <section className="admin-section">
          <div className="admin-section-top">
            <h2>오늘의 뉴스 관리</h2>
            <p>등록된 오늘의 뉴스 목록은 추후 이 영역에서 관리하면 됩니다.</p>
          </div>
          <div className="admin-section-card admin-empty-card">
            <AdminTodayNewsList />
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