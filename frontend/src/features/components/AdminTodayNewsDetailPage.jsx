import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../../api";
import toast from "react-hot-toast";
import "@/features/css/AdminTodayNewsDetailPage.css";

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

  useEffect(() => {
    fetchTodayNewsDetail();
  }, [id]);

  async function handleUpdate() {
    if (saving) return;

    if (!summaryTitle.trim() || !summaryText.trim()) {
      toast.error("제목과 요약문을 입력해 주세요.");
      return;
    }

    try {
      setSaving(true);

      await api.put(`/api/admin/todayNews/${id}`, {
        summaryTitle,
        summaryText,
        isPublished,
      });

      toast.success("수정이 완료되었습니다.");
      navigate("/admin");
      fetchTodayNewsDetail();
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
      summaryTitle,
      summaryText,
      isPublished: nextValue,
    });

    setIsPublished(nextValue);
    toast.success(nextValue ? "공개 게시되었습니다." : "게시가 취소되었습니다.");
  } catch (err) {
    toast.error(err?.response?.data?.message || "상태 변경에 실패했습니다.");
  }
}

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
  onClick={() => handleTogglePublished()}
  className="admin-today-news-detail-btn publish"
>
  {isPublished ? "게시 취소로 변경" : "공개 게시로 변경"}
</button>
        </div>
      </div>
    </div>
  );
}