import { useEffect, useState } from "react"
import { useParams } from "react-router-dom";
import api from "../../api"
import toast from "react-hot-toast"
import '@/features/css/TodayNewsDetailPage.css'

export default function SummaryDetailPage() {
    const {id} = useParams();

    const [summaryTitle, setSummaryTitle] = useState("");
    const [summaryText, setSummaryText] = useState("");
    const [createdAt, setCreatedAt] = useState("");
    const [loading, setLoading] = useState(false);

    async function fetchDetails() {
        try {
            setLoading(true);
            const res = await api.get(`/api/announcements/${id}`);
            setSummaryText(res.data.summaryText);
            setSummaryTitle(res.data.summaryTitle);
            setCreatedAt(res.data.createdAt);
        } catch(e) {
            toast.error(e?.response?.data?.message || "조회에 실패했습니다.");
        }  finally {
            setLoading(false);
        }
         
    }

    useEffect(()=> {
        fetchDetails();
    }, [])

      if (loading) {
    return (
      <div className="summary-detail-page">
        <div className="summary-detail-card">
          <p className="summary-detail-loading">불러오는 중...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="summary-detail-page">
      <div className="summary-detail-card">
        <div className="summary-detail-header">
          <span className="summary-detail-badge">오늘의 뉴스</span>
          <h1 className="summary-detail-title">{summaryTitle}</h1>
          <p className="summary-detail-date">{createdAt}</p>
        </div>

        <div className="summary-detail-divider"></div>

        <div className="summary-detail-body">
          <h2 className="summary-detail-subtitle">요약 내용</h2>
          <p className="summary-detail-text">{summaryText}</p>
        </div>
      </div>
    </div>
  );
}