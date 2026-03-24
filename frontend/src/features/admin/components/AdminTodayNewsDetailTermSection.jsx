import { Link } from "react-router-dom";
import { useEffect, useMemo, useState } from "react";
import api from "@/api";
import toast from "react-hot-toast";
import handleApiError from "@/shared/utils/handleApiError";

export default function AdminTodayNewsDetailTermSection({ id }) {
  const [allTerms, setAllTerms] = useState([]);
  const [linkedTerms, setLinkedTerms] = useState([]);
  const [selectedTermId, setSelectedTermId] = useState("");
  const [termLoading, setTermLoading] = useState(true);
  const [linking, setLinking] = useState(false);
  const [unlinkingId, setUnlinkingId] = useState(null);

  async function fetchAllTerms() {
    try {
      const res = await api.get("/api/admin/terms");
      setAllTerms(Array.isArray(res.data) ? res.data : []);
    } catch (e) {
      handleApiError(e, "조회 실패");
      setAllTerms([]);
    }
  }

  async function fetchLinkedTerms() {
    try {
      setTermLoading(true);
      const res = await api.get(`/api/admin/todayNews/${id}/terms`);
      setLinkedTerms(Array.isArray(res.data) ? res.data : []);
    } catch (e) {
      handleApiError(e, "조회 실패");
      setLinkedTerms([]);
    } finally {
      setTermLoading(false);
    }
  }

  async function fetchTermSectionData() {
    await Promise.all([fetchAllTerms(), fetchLinkedTerms()]);
  }

  useEffect(() => {
    if (!id) return;
    fetchTermSectionData();
  }, [id]);

  async function handleLinkTerm() {
    if (!selectedTermId) {
      toast.error("연결할 단어를 선택해 주세요.");
      return;
    }

    if (linking) return;

    try {
      setLinking(true);

      await api.post(`/api/admin/todayNews/${id}/terms`, {
        termId: Number(selectedTermId),
      });

      toast.success("단어가 연결되었습니다.");
      setSelectedTermId("");
      await fetchLinkedTerms();
    } catch (e) {
      handleApiError(e, "연결 실패");
    } finally {
      setLinking(false);
    }
  }

  async function handleUnlinkTerm(termId) {
    if (unlinkingId) return;

    try {
      setUnlinkingId(termId);

      await api.delete(`/api/admin/todayNews/${id}/terms/${termId}`);

      toast.success("단어 연결이 해제되었습니다.");
      await fetchLinkedTerms();
    } catch (e) {
      handleApiError(e, "해제 실패");
    } finally {
      setUnlinkingId(null);
    }
  }

  const availableTerms = useMemo(() => {
    const linkedIds = new Set(linkedTerms.map((term) => term.id));
    return allTerms.filter((term) => !linkedIds.has(term.id));
  }, [allTerms, linkedTerms]);

  return (
    <div className="admin-today-news-term-card">
      <div className="admin-today-news-term-top">
        <div>
          <h2 className="admin-today-news-term-title">연결된 관리자 단어</h2>
          <p className="admin-today-news-term-subtitle">
            오늘의 뉴스 상세에 보여줄 단어를 연결할 수 있습니다.
          </p>
        </div>

        <Link to="/admin/terms" className="btn btn-outline">
          단어장 관리
        </Link>
      </div>

      <div className="admin-today-news-term-link-box">
        <select
          value={selectedTermId}
          onChange={(e) => setSelectedTermId(e.target.value)}
          className="admin-today-news-term-select"
        >
          <option value="">연결할 단어를 선택하세요</option>
          {availableTerms.map((term) => (
            <option key={term.id} value={term.id}>
              {term.term}
            </option>
          ))}
        </select>

        <button
          type="button"
          onClick={handleLinkTerm}
          disabled={linking || availableTerms.length === 0}
          className="btn btn-primary"
        >
          {linking ? "연결 중..." : "단어 연결"}
        </button>
      </div>

      {availableTerms.length === 0 && (
        <p className="admin-today-news-term-guide">
          연결 가능한 단어가 없습니다. 이미 모두 연결되었거나 관리자 단어장이 비어
          있습니다.
        </p>
      )}

      <div className="admin-today-news-term-list-wrap">
        <h3 className="admin-today-news-term-list-title">
          현재 연결된 단어 ({linkedTerms.length})
        </h3>

        {termLoading ? (
          <p className="admin-today-news-term-empty">불러오는 중...</p>
        ) : linkedTerms.length === 0 ? (
          <p className="admin-today-news-term-empty">
            아직 연결된 단어가 없습니다.
          </p>
        ) : (
          <div className="admin-today-news-term-list">
            {linkedTerms.map((term) => (
              <div key={term.id} className="admin-today-news-term-item">
                <div className="admin-today-news-term-item-content">
                  <p className="admin-today-news-term-word">{term.term}</p>
                  <p className="admin-today-news-term-definition">
                    {term.definition}
                  </p>
                </div>

                <button
                  type="button"
                  onClick={() => handleUnlinkTerm(term.id)}
                  disabled={unlinkingId === term.id}
                  className="btn btn-danger"
                >
                  {unlinkingId === term.id ? "해제 중..." : "연결 해제"}
                </button>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}