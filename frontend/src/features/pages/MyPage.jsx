import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api"
import toast from "react-hot-toast";
import "@/features/css/MyPage.css";
import MySummaries from "../components/MySummaries";
import MyTerms from "../components/MyTerms";

export default function MyPage() {
  const nav = useNavigate();

  const [user, setUser] = useState(null);

  async function fetchMyPage() {
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
    if (!token) nav("/login");
    else fetchMyPage();
  }, []);

  function logout() {
    localStorage.removeItem("accessToken");
    toast.success("로그아웃 되었습니다.");
    nav("/today");
  }

  function goMySummaries() {
    nav("/summaries");
  }

  function goMyTerms() {
    nav("/terms");
  }

  function goPreferences() {
    nav("/me/preferences");
  }

  function withdraw() {
    toast("회원탈퇴 기능은 아직 준비 중입니다.");
  }

  if (!user) {
    return (
      <div className="mypage-loading">
        <p>로딩 중...</p>
      </div>
    );
  }

  return (
    <div className="mypage-page">
      <div className="mypage-container">
        <header className="mypage-header">
          <p className="mypage-label">My Account</p>
          <h1>마이페이지</h1>
          <p className="mypage-desc">
            내 정보와 저장한 콘텐츠를 한 곳에서 관리해보세요.
          </p>
        </header>

        <section className="profile-card">
          <div className="profile-top">
            <div>
              <p className="profile-subtitle">내 정보</p>
              <h2 className="profile-name">{user.displayName || user.userName}</h2>
            </div>
            <span className="profile-badge">회원</span>
          </div>

          <div className="profile-info-list">
            <div className="profile-info-item">
              <span className="profile-info-label">이메일</span>
              <p>{user.email}</p>
            </div>

            <div className="profile-info-item">
              <span className="profile-info-label">이름</span>
              <p>{user.userName}</p>
            </div>

            <div className="profile-info-item">
              <span className="profile-info-label">별명</span>
              <p>{user.displayName}</p>
            </div>
          </div>
        </section>

        <section className="mypage-menu-section">
          <h2 className="mypage-section-title">내 활동</h2>

          <div className="mypage-menu-grid">
            <button className="menu-card" onClick={goMySummaries} type="button">
              <div>
                <h3>내 기사</h3>
                <p>저장한 기사 요약을 확인하고 관리합니다.</p>
              </div>
              <span>→</span>
            </button>

            <button className="menu-card" onClick={goMyTerms} type="button">
              <div>
                <h3>내 단어장</h3>
                <p>저장한 경제 용어와 정의를 정리합니다.</p>
              </div>
              <span>→</span>
            </button>

            <button className="menu-card" onClick={goPreferences} type="button">
              <div>
                <h3>내 선호도</h3>
                <p>요약 스타일과 개인 설정을 확인합니다.</p>
              </div>
              <span>→</span>
            </button>
          </div>
        </section>

        <section className="mypage-account-section">
          <h2 className="mypage-section-title">계정 관리</h2>

          <div className="account-actions">
            <button className="logout-btn" onClick={logout} type="button">
              로그아웃
            </button>
            <button className="withdraw-btn" onClick={withdraw} type="button">
              회원탈퇴
            </button>
          </div>
        </section>
      </div>
    </div>
  );
}