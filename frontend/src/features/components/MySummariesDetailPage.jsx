import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../../api";
import toast from "react-hot-toast";
import "@/features/css/MySummariesDetailPage.css";

export default function MySummariesDetailPage() {
  const { id } = useParams();

  const [summaryTitle, setSummaryTitle] = useState("");
  const [summaryText, setSummaryText] = useState("");
  const [createdAt, setCreatedAt] = useState("");
  const [loading, setLoading] = useState(false);

  const [myTerms, setMyTerms] = useState([]);
  const [selectedTerm, setSelectedTerm] = useState(null);

  async function fetchDetails() {
    try {
      setLoading(true);
      const res = await api.get(`/api/me/summaries/${id}`);
      setSummaryText(res.data.summaryText || "");
      setSummaryTitle(res.data.summaryTitle || "");
      setCreatedAt(res.data.createdAt || "");
    } catch (e) {
      toast.error(e?.response?.data?.message || "조회에 실패했습니다.");
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
      ? new RegExp(`(${words.map((word) => word.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")).join("|")})`, "g")
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
            {createdAt ? new Date(createdAt).toLocaleDateString("ko-KR") : ""}
          </p>
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
                    onClick={() => setSelectedTerm((prev) =>
                        prev?.term === foundTerm.term ? null : foundTerm)}
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
        </div>
      </div>
    </div>
  );
}