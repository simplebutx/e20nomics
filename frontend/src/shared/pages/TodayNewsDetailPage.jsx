import { useEffect, useMemo, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../../api";
import toast from "react-hot-toast";
import "@/shared/css/TodayNewsDetailPage.css";

export default function TodayNewsDetailPage() {
  const { id } = useParams();

  const [summaryTitle, setSummaryTitle] = useState("");
  const [summaryText, setSummaryText] = useState("");
  const [createdAt, setCreatedAt] = useState("");

  const [linkedTerms, setLinkedTerms] = useState([]);
  const [myTerms, setMyTerms] = useState([]);

  const [loading, setLoading] = useState(false);
  const [selectedTerm, setSelectedTerm] = useState(null);

  async function fetchDetails() {
    try {
      setLoading(true);

      const res = await api.get(`/api/todayNews/${id}`);

      setSummaryTitle(res.data.summaryTitle || "");
      setSummaryText(res.data.summaryText || "");
      setCreatedAt(res.data.createdAt || "");
      setLinkedTerms(Array.isArray(res.data.terms) ? res.data.terms : []);
    } catch (e) {
      toast.error(e?.response?.data?.message || "상세 페이지 불러오기 실패.");
    } finally {
      setLoading(false);
    }
  }

  async function fetchMyTerms() {
    try {
      const res = await api.get("/api/me/terms");
      setMyTerms(Array.isArray(res.data) ? res.data : []);
    } catch (e) {
      toast.error(e?.response?.data?.message || "내 단어장 불러오기 실패.");
    }
  }

  useEffect(() => {
    if (!id) return;
    fetchDetails();
    fetchMyTerms();
  }, [id]);

  function escapeRegExp(text) {
    return text.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
  }

  const mergedTerms = useMemo(() => {
    const map = new Map();

    linkedTerms.forEach((item) => {
      if (!item?.term) return;

      map.set(item.term, {
        termId: item.termId,
        term: item.term,
        definition: item.definition,
        source: "linked",
      });
    });

    myTerms.forEach((item) => {
      if (!item?.term) return;

      const existing = map.get(item.term);

      if (existing) {
        map.set(item.term, {
          ...existing,
          myDefinition: item.definition,
          source: "both",
        });
      } else {
        map.set(item.term, {
          termId: item.id,
          term: item.term,
          definition: item.definition,
          source: "my",
        });
      }
    });

    return Array.from(map.values());
  }, [linkedTerms, myTerms]);

  const matchedTerms = useMemo(() => {
    if (!summaryText || !Array.isArray(mergedTerms)) return [];

    return mergedTerms.filter(
      (item) => item?.term && summaryText.includes(item.term)
    );
  }, [summaryText, mergedTerms]);

  const parts = useMemo(() => {
    if (!summaryText) return [];

    const words = matchedTerms
      .map((item) => item.term)
      .filter(Boolean)
      .sort((a, b) => b.length - a.length)
      .map((word) => escapeRegExp(word));

    if (words.length === 0) {
      return [summaryText];
    }

    const regex = new RegExp(`(${words.join("|")})`, "g");
    return summaryText.split(regex);
  }, [summaryText, matchedTerms]);

  function formatDate(dateString) {
    if (!dateString) return "";
    return new Date(dateString).toLocaleString("ko-KR");
  }

  const isAlreadySaved =
  selectedTerm &&
  myTerms.some((item) => item.term === selectedTerm.term);

  async function handleSaveMyTerm() {
  if (!selectedTerm) return;

  try {
    await api.post("/api/me/terms", {
      term: selectedTerm.term,
      definition: selectedTerm.definition,
    });

    toast.success("내 단어장에 저장했습니다.");
    await fetchMyTerms();

    setSelectedTerm((prev) =>
      prev
        ? {
            ...prev,
            source: "both",
            myDefinition: prev.definition,
          }
        : prev
    );
  } catch (e) {
    toast.error(e?.response?.data?.message || "단어 저장에 실패했습니다.");
  }
}

  if (loading) {
    return (
      <div className="today-news-detail-page">
        <div className="summary-detail-card">
          <p className="summary-detail-loading">불러오는 중...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="today-news-detail-page">
      <div className="summary-detail-card">
        <div className="summary-detail-header">
          <span className="summary-detail-badge">오늘의 뉴스</span>
          <h1 className="summary-detail-title">{summaryTitle}</h1>
          <p className="summary-detail-date">{formatDate(createdAt)}</p>
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
                    className={`term-link ${foundTerm.source}`}
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
            <div className={`term-definition-card ${selectedTerm.source}`}>
              <h3>{selectedTerm.term}</h3>

              {selectedTerm.source === "linked" && (
  <>
    <p>{selectedTerm.definition}</p>
    <small className="term-source-label">출처 · 오늘의 뉴스</small>

    <div className="term-card-action">
      <button
        type="button"
        className="term-save-button"
        onClick={handleSaveMyTerm}
        disabled={isAlreadySaved}
      >
        {isAlreadySaved ? "이미 저장됨" : "내 단어장에 저장"}
      </button>
    </div>
  </>
)}

              {selectedTerm.source === "my" && (
                <>
                  <p>{selectedTerm.definition}</p>
                  <small className="term-source-label">출처 - 내 단어장</small>
                </>
              )}

              {selectedTerm.source === "both" && (
                <>
                  <p>
                    <strong>오늘의 뉴스 단어 뜻:</strong>{" "}
                    {selectedTerm.definition}
                  </p>
                  <p>
                    <strong>내 단어장 뜻:</strong>{" "}
                    {selectedTerm.myDefinition}
                  </p>
                  <small className="term-source-label">
                    출처 - 오늘의 뉴스 + 내 단어장
                  </small>
                </>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}