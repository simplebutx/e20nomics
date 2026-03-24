import { useEffect, useMemo, useState } from "react";
import { useParams } from "react-router-dom";
import api from "@/api";
import toast from "react-hot-toast";
import handleApiError from "@/shared/utils/handleApiError";
import HighlightedSummaryText from "@/shared/components/HighlightedSummaryText";
import TermDefinitionCard from "@/shared/components/TermDefinitionCard";
import "@/shared/css/TodayNewsDetailPage.css";
import "@/shared/css/Button.css";

export default function TodayNewsDetailPage() {
  const { id } = useParams();

  const [summaryTitle, setSummaryTitle] = useState("");
  const [summaryText, setSummaryText] = useState("");
  const [createdAt, setCreatedAt] = useState("");
  const [imageKey, setImageKey] = useState("");

  const [linkedTerms, setLinkedTerms] = useState([]);
  const [myTerms, setMyTerms] = useState([]);

  const [loading, setLoading] = useState(false);
  const [selectedTerm, setSelectedTerm] = useState(null);

  const baseImageUrl = import.meta.env.VITE_IMAGE_BASE_URL || "";
  const imageUrl = imageKey
    ? `${baseImageUrl.replace(/\/$/, "")}/${imageKey}`
    : "";

  async function fetchDetails() {
    try {
      setLoading(true);

      const res = await api.get(`/api/todayNews/${id}`);

      setSummaryTitle(res.data.summaryTitle || "");
      setSummaryText(res.data.summaryText || "");
      setCreatedAt(res.data.createdAt || "");
      setImageKey(res.data.imageKey || "");
      setLinkedTerms(Array.isArray(res.data.terms) ? res.data.terms : []);
    } catch (e) {
      handleApiError(e, "조회 실패");
    } finally {
      setLoading(false);
    }
  }

  async function fetchMyTerms() {
    try {
      const res = await api.get("/api/me/terms");
      setMyTerms(Array.isArray(res.data) ? res.data : []);
    } catch (e) {
      handleApiError(e, "조회 실패");
    }
  }

  useEffect(() => {
    if (!id) return;
    fetchDetails();
    fetchMyTerms();
  }, [id]);

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
      handleApiError(e, "저장 실패");
    }
  }

  function formatDate(dateString) {
    if (!dateString) return "";
    return new Date(dateString).toLocaleString("ko-KR");
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
          {imageUrl && (
            <div className="admin-today-news-detail-image-wrap">
              <img
                src={imageUrl}
                alt={summaryTitle || "오늘의 뉴스 대표 이미지"}
                className="admin-today-news-detail-image"
              />
            </div>
          )}
        </div>

        <div className="summary-detail-divider" />

        <div className="summary-detail-body">
          <h2 className="summary-detail-subtitle">요약 내용</h2>

          <HighlightedSummaryText
            summaryText={summaryText}
            matchedTerms={matchedTerms}
            selectedTerm={selectedTerm}
            onSelectTerm={setSelectedTerm}
          />

          {selectedTerm && (
            <TermDefinitionCard
              selectedTerm={selectedTerm}
              isAlreadySaved={isAlreadySaved}
              onSaveMyTerm={handleSaveMyTerm}
            />
          )}
        </div>
      </div>
    </div>
  );
}
