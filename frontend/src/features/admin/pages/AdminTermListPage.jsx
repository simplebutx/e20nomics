import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "@/api";
import toast from "react-hot-toast";
import AdminTermCreate from "@/features/admin/components/AdminTermCreate";
import handleApiError from "@/shared/utils/handleApiError";
import "@/features/admin/css/AdminTermPage.css";
import "@/shared/css/Button.css";

export default function AdminTermListPage() {
  const [terms, setTerms] = useState([]);
  const [loading, setLoading] = useState(true);

  async function fetchTerms() {
    try {
      setLoading(true);
      const res = await api.get("/api/admin/terms");
      setTerms(Array.isArray(res.data) ? res.data : []);
    } catch (e) {
      handleApiError(e, "조회 실패");
      setTerms([]);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    fetchTerms();
  }, []);

  function formatDate(dateString) {
    if (!dateString) return "";
    return new Date(dateString).toLocaleString("ko-KR");
  }

  return (
    <div className="admin-term-page">
      <div className="admin-term-container">
        <div className="admin-term-header">
          <h1 className="admin-term-page-title">관리자 단어장</h1>
          <p className="admin-term-page-subtitle">
            오늘의 뉴스에 연결할 단어를 등록하고 관리할 수 있습니다.
          </p>
        </div>

        <AdminTermCreate onCreated={fetchTerms} />

        <div className="admin-term-list-card">
          <div className="admin-term-list-top">
            <h2 className="admin-term-list-title">등록된 단어 목록</h2>
            <span className="admin-term-count">총 {terms.length}개</span>
          </div>

          {loading ? (
            <p className="admin-term-loading">불러오는 중...</p>
          ) : terms.length === 0 ? (
            <p className="admin-term-empty">등록된 단어가 없습니다.</p>
          ) : (
            <div className="admin-term-list">
              {terms.map((term) => (
                <Link
                  to={`/admin/terms/${term.id}`}
                  key={term.id}
                  className="admin-term-item"
                >
                  <div className="admin-term-item-top">
                    <h3 className="admin-term-item-word">{term.term}</h3>
                    <span className="admin-term-item-date">
                      {formatDate(term.createdAt)}
                    </span>
                  </div>

                  <p className="admin-term-item-definition">
                    {term.definition}
                  </p>
                </Link>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}