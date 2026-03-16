import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import api from "@/api";
import toast from "react-hot-toast";
import "@/features/admin/css/AdminTodayNewsDetailPage.css";

export default function AdminTodayNewsDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [summaryTitle, setSummaryTitle] = useState("");
  const [summaryText, setSummaryText] = useState("");
  const [isPublished, setIsPublished] = useState(false);
  const [createdAt, setCreatedAt] = useState("");

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [deleting, setDeleting] = useState(false);

  // 단어 관련 state
  const [allTerms, setAllTerms] = useState([]);
  const [linkedTerms, setLinkedTerms] = useState([]);
  const [selectedTermId, setSelectedTermId] = useState("");
  const [termLoading, setTermLoading] = useState(false);
  const [linking, setLinking] = useState(false);
  const [unlinkingId, setUnlinkingId] = useState(null);

  async function fetchTodayNewsDetail() {
    try {
      setLoading(true);
      const res = await api.get(`/api/admin/todayNews/${id}`);

      setSummaryTitle(res.data.summaryTitle || "");
      setSummaryText(res.data.summaryText || "");
      setIsPublished(!!res.data.isPublished);
      setCreatedAt(res.data.createdAt || "");
    } catch (err) {
      toast.error(err?.response?.data?.message || "상세 조회에 실패했습니다.");
    } finally {
      setLoading(false);
    }
  }

  async function fetchAllTerms() {   // 관리자 단어 불러오기
    try {
      const res = await api.get("/api/admin/terms");
      setAllTerms(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      toast.error(err?.response?.data?.message || "관리자 단어 목록 조회에 실패했습니다.");
      setAllTerms([]);
    }
  }

  async function fetchLinkedTerms() {   // 현재뉴스와 연결왜있는 단어목록 조회
    try {
      setTermLoading(true);
      const res = await api.get(`/api/admin/todayNews/${id}/terms`);
      setLinkedTerms(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      toast.error(err?.response?.data?.message || "연결된 단어 조회에 실패했습니다.");
      setLinkedTerms([]);
    } finally {
      setTermLoading(false);
    }
  }

  async function fetchPageData() {
    await Promise.all([
      fetchTodayNewsDetail(),
      fetchAllTerms(),
      fetchLinkedTerms(),
    ]);
  }

  useEffect(() => {
    fetchPageData();
  }, [id]);

  async function handleUpdate() {
    if (saving) return;

    if (!summaryTitle.trim() || !summaryText.trim()) {
      toast.error("제목과 요약문을 입력해 주세요.");
      return;
    }

    try {
      setSaving(true);

      await api.put(`/api/admin/todayNews/${id}`, {    // 현재뉴스와 단어 연결하기
        summaryTitle: summaryTitle.trim(),
        summaryText: summaryText.trim(),
        isPublished,
      });

      toast.success("수정이 완료되었습니다.");
      await fetchTodayNewsDetail();
    } catch (err) {
      toast.error(err?.response?.data?.message || "수정에 실패했습니다.");
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete() {
    if (deleting) return;

    const ok = window.confirm("정말 삭제하시겠습니까?");
    if (!ok) return;

    try {
      setDeleting(true);
      await api.delete(`/api/admin/todayNews/${id}`);   
      toast.success("삭제되었습니다.");
      navigate("/admin");
    } catch (err) {
      toast.error(err?.response?.data?.message || "삭제에 실패했습니다.");
    } finally {
      setDeleting(false);
    }
  }

  async function handleTogglePublished() {
    const nextValue = !isPublished;

    try {
      await api.put(`/api/admin/todayNews/${id}`, {   
        summaryTitle: summaryTitle.trim(),
        summaryText: summaryText.trim(),
        isPublished: nextValue,
      });

      setIsPublished(nextValue);
      toast.success(nextValue ? "공개 게시되었습니다." : "게시가 취소되었습니다.");
    } catch (err) {
      toast.error(err?.response?.data?.message || "상태 변경에 실패했습니다.");
    }
  }

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
    } catch (err) {
      toast.error(err?.response?.data?.message || "단어 연결에 실패했습니다.");
    } finally {
      setLinking(false);
    }
  }

  async function handleUnlinkTerm(termId) {
    if (unlinkingId) return;

    try {
      setUnlinkingId(termId);

      await api.delete(`/api/admin/todayNews/${id}/terms/${termId}`);   // 현재뉴스와 단어 연결 해제하기

      toast.success("단어 연결이 해제되었습니다.");
      await fetchLinkedTerms();
    } catch (err) {
      toast.error(err?.response?.data?.message || "단어 연결 해제에 실패했습니다.");
    } finally {
      setUnlinkingId(null);
    }
  }

  const availableTerms = useMemo(() => {
    const linkedIds = new Set(linkedTerms.map((term) => term.id));
    return allTerms.filter((term) => !linkedIds.has(term.id));
  }, [allTerms, linkedTerms]);

  function formatDate(dateString) {
    if (!dateString) return "";
    return new Date(dateString).toLocaleString("ko-KR");
  }

  if (loading) {
    return (
      <div className="admin-today-news-detail-page">
        <div className="admin-today-news-detail-card">
          <p className="admin-today-news-detail-loading">불러오는 중...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="admin-today-news-detail-page">
      <div className="admin-today-news-detail-card">
        <div className="admin-today-news-detail-top">
          <h1 className="admin-today-news-detail-title">오늘의 뉴스 상세</h1>
          <span
            className={
              isPublished
                ? "admin-today-news-status published"
                : "admin-today-news-status unpublished"
            }
          >
            {isPublished ? "게시됨" : "미게시"}
          </span>
        </div>

        <p className="admin-today-news-detail-date">
          등록일: {formatDate(createdAt)}
        </p>

        <div className="admin-today-news-detail-field">
          <label className="admin-today-news-detail-label">제목</label>
          <input
            type="text"
            value={summaryTitle}
            onChange={(e) => setSummaryTitle(e.target.value)}
            className="admin-today-news-detail-input"
            placeholder="제목을 입력하세요"
          />
        </div>

        <div className="admin-today-news-detail-field">
          <label className="admin-today-news-detail-label">요약문</label>
          <textarea
            value={summaryText}
            onChange={(e) => setSummaryText(e.target.value)}
            className="admin-today-news-detail-textarea"
            placeholder="요약문을 입력하세요"
          />
        </div>

        <div className="admin-today-news-detail-buttons">
          <button
            type="button"
            onClick={handleUpdate}
            disabled={saving}
            className="admin-today-news-detail-btn edit"
          >
            {saving ? "수정 중..." : "수정하기"}
          </button>

          <button
            type="button"
            onClick={handleDelete}
            disabled={deleting}
            className="admin-today-news-detail-btn delete"
          >
            {deleting ? "삭제 중..." : "삭제하기"}
          </button>

          <button
            type="button"
            onClick={() => navigate("/admin/todayNews")}
            className="admin-today-news-detail-btn list"
          >
            목록으로
          </button>

          <button
            type="button"
            onClick={handleTogglePublished}
            className="admin-today-news-detail-btn publish"
          >
            {isPublished ? "게시 취소로 변경" : "공개 게시로 변경"}
          </button>
        </div>
      </div>

      {/* 단어 연결 UI */}
      <div className="admin-today-news-term-card">
        <div className="admin-today-news-term-top">
          <div>
            <h2 className="admin-today-news-term-title">연결된 관리자 단어</h2>
            <p className="admin-today-news-term-subtitle">
              오늘의 뉴스 상세에 보여줄 단어를 연결할 수 있습니다.
            </p>
          </div>

          <Link to="/admin/terms" className="admin-today-news-term-manage-link">
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
            className="admin-today-news-term-link-btn"
          >
            {linking ? "연결 중..." : "단어 연결"}
          </button>
        </div>

        {availableTerms.length === 0 && (
          <p className="admin-today-news-term-guide">
            연결 가능한 단어가 없습니다. 이미 모두 연결되었거나 관리자 단어장이 비어 있습니다.
          </p>
        )}

        <div className="admin-today-news-term-list-wrap">
          <h3 className="admin-today-news-term-list-title">
            현재 연결된 단어 ({linkedTerms.length})
          </h3>

          {termLoading ? (
            <p className="admin-today-news-term-empty">불러오는 중...</p>
          ) : linkedTerms.length === 0 ? (
            <p className="admin-today-news-term-empty">아직 연결된 단어가 없습니다.</p>
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
                    className="admin-today-news-term-unlink-btn"
                  >
                    {unlinkingId === term.id ? "해제 중..." : "연결 해제"}
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}