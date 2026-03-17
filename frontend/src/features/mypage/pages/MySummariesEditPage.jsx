import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "@/api";
import toast from "react-hot-toast";
import "@/features/mypage/css/MySummariesEditPage.css";
import "@/shared/css/button.css";

export default function MySummariesEditPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [summaryTitle, setSummaryTitle] = useState("");
  const [summaryText, setSummaryText] = useState("");
  const [memo, setMemo] = useState("");
  const [createdAt, setCreatedAt] = useState("");

  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);

  async function fetchSummary() {
    try {
      setLoading(true);
      const res = await api.get(`/api/me/summaries/${id}`);
      const data = res.data;

      setSummaryTitle(data.summaryTitle || "");
      setSummaryText(data.summaryText || "");
      setMemo(data.memo || "");
      setCreatedAt(data.createdAt || "");
    } catch (e) {
      toast.error(e?.response?.data?.message || "요약을 불러오지 못했습니다.");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    if (!id) return;
    fetchSummary();
  }, [id]);

  async function handleSubmit(e) {
    e.preventDefault();

    if (saving) return;

    if (!summaryTitle.trim()) {
      toast.error("제목을 입력해 주세요.");
      return;
    }

    if (!summaryText.trim()) {
      toast.error("요약 내용을 입력해 주세요.");
      return;
    }

    try {
      setSaving(true);

      await api.patch(`/api/me/summaries/${id}`, {
        summaryTitle,
        summaryText,
        memo,
      });

      toast.success("수정되었습니다.");
      navigate(`/summaries/${id}`);
    } catch (e) {
      toast.error(e?.response?.data?.message || "수정에 실패했습니다.");
    } finally {
      setSaving(false);
    }
  }

  function handleCancel() {
    navigate(`/summaries/${id}`);
  }

  if (loading) {
    return (
      <div className="my-summary-edit-page">
        <div className="summary-edit-card">
          <p className="summary-edit-loading">불러오는 중...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="my-summary-edit-page">
      <div className="summary-edit-card">
        <div className="summary-edit-header">
          <span className="summary-edit-badge">요약 수정</span>
          <h1 className="summary-edit-title">내 요약 수정하기</h1>

          <p className="summary-edit-date">
            {createdAt
              ? new Date(createdAt).toLocaleString("ko-KR", {
                  year: "numeric",
                  month: "2-digit",
                  day: "2-digit",
                  hour: "2-digit",
                  minute: "2-digit",
                  hour12: false,
                })
              : ""}
          </p>
        </div>

        <div className="summary-edit-divider"></div>

        <form className="summary-edit-form" onSubmit={handleSubmit}>
          <div className="summary-edit-group">
            <label className="summary-edit-label">제목</label>
            <input
              type="text"
              className="summary-edit-input"
              value={summaryTitle}
              onChange={(e) => setSummaryTitle(e.target.value)}
              placeholder="제목을 입력하세요"
            />
          </div>

          <div className="summary-edit-group">
            <label className="summary-edit-label">요약 내용</label>
            <textarea
              className="summary-edit-textarea"
              value={summaryText}
              onChange={(e) => setSummaryText(e.target.value)}
              placeholder="요약 내용을 입력하세요"
              rows={10}
            />
          </div>

          <div className="summary-edit-group">
            <label className="summary-edit-label">메모</label>
            <textarea
              className="summary-edit-textarea memo"
              value={memo}
              onChange={(e) => setMemo(e.target.value)}
              placeholder="이 기사에 대한 내 메모를 작성해 보세요"
              rows={6}
            />
          </div>

          <div className="summary-edit-actions">
            <button
              type="submit"
              className="btn btn-primary"
              disabled={saving}
            >
              {saving ? "저장 중..." : "저장"}
            </button>

            <button
              type="button"
              className="btn btn-secondary"
              onClick={handleCancel}
              disabled={saving}
            >
              취소
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}