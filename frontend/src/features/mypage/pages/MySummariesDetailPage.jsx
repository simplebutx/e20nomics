import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "@/api";
import toast from "react-hot-toast";
import handleApiError from "@/shared/utils/handleApiError";
import "@/features/mypage/css/MySummariesDetailPage.css";
import "@/shared/css/Button.css";

export default function MySummariesDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [summaryTitle, setSummaryTitle] = useState("");
  const [summaryText, setSummaryText] = useState("");
  const [memo, setMemo] = useState("");
  const [createdAt, setCreatedAt] = useState("");
  const [loading, setLoading] = useState(false);
  const [deleting, setDeleting] = useState(false);

  const [myTerms, setMyTerms] = useState([]);
  const [selectedTerm, setSelectedTerm] = useState(null);

  async function fetchDetails() {
    try {
      setLoading(true);
      const res = await api.get(`/api/me/summaries/${id}`);
      setSummaryText(res.data.summaryText || "");
      setSummaryTitle(res.data.summaryTitle || "");
      setMemo(res.data.memo || "");
      setCreatedAt(res.data.createdAt || "");
    } catch (e) {
      handleApiError(e, "페이지 불러오기 실패");
    } finally {
      setLoading(false);
    }
  }

  async function fetchMyTerms() {
    try {
      const res = await api.get("/api/me/terms");
      const data = res.data;
      setMyTerms(Array.isArray(data) ? data : []);
    } catch (e) {
      toast.error(e?.response?.data?.message || "단어장 불러오기 실패");
    }
  }

  async function handleDelete() {
    if (deleting) return;

    const ok = window.confirm("이 요약을 삭제하시겠습니까?");
    if (!ok) return;

    try {
      setDeleting(true);
      await api.delete(`/api/me/summaries/${id}`);
      toast.success("삭제되었습니다.");
      navigate("/summaries");
    } catch (e) {
      handleApiError(e, "삭제 실패");
    } finally {
      setDeleting(false);
    }
  }

  useEffect(() => {
    if (!id) return;
    fetchDetails();
    fetchMyTerms();
  }, [id]);

  const matchedTerms = myTerms.filter((item) => summaryText.includes(item.term));

  const words = matchedTerms
    .map((item) => item.term)
    .sort((a, b) => b.length - a.length);

  const regex =
    words.length > 0
      ? new RegExp(
          `(${words
            .map((word) => word.replace(/[.*+?^${}()|[\]\\]/g, "\\$&"))
            .join("|")})`,
          "g"
        )
      : null;

  const parts = regex ? summaryText.split(regex) : [summaryText];

  if (loading) {
    return (
      <div className="my-summary-detail-page">
        <div className="summary-detail-card">
          <p className="summary-detail-loading">불러오는 중...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="my-summary-detail-page">
      <div className="summary-detail-card">
        <div className="summary-detail-header">
          <span className="summary-detail-badge">내 요약</span>
          <h1 className="summary-detail-title">{summaryTitle}</h1>

          <p className="summary-detail-date">
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

          <div className="summary-detail-actions">
            <button
              className="btn btn-outline"
              onClick={() => navigate(`/summaries/${id}/edit`)}
              type="button"
            >
              수정
            </button>

            <button
              className="btn btn-danger"
              onClick={handleDelete}
              disabled={deleting}
              type="button"
            >
              {deleting ? "삭제 중..." : "삭제"}
            </button>
          </div>
        </div>

        <div className="summary-detail-divider"></div>

        <div className="summary-detail-body">
          <h2 className="summary-detail-subtitle">요약 내용</h2>

          <p className="summary-detail-text">
            {parts.map((part, index) => {
              const foundTerm = matchedTerms.find((item) => item.term === part);

              if (foundTerm) {
                return (
                  <span
                    key={index}
                    className="term-link"
                    onClick={() =>
                      setSelectedTerm((prev) =>
                        prev?.term === foundTerm.term ? null : foundTerm
                      )
                    }
                  >
                    {part}
                  </span>
                );
              }

              return <span key={index}>{part}</span>;
            })}
          </p>

          {selectedTerm && (
            <div className="term-definition-card">
              <h3>{selectedTerm.term}</h3>
              <p>{selectedTerm.definition}</p>
            </div>
          )}

          <div className="summary-detail-memo-section">
            <h2 className="summary-detail-subtitle">메모</h2>
            <div className="summary-detail-memo-box">
              {memo?.trim() ? memo : "작성된 메모가 없습니다."}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}