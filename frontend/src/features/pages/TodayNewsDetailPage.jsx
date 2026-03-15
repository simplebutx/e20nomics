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

    const [myTerms, setMyTerms] = useState([]);
    const [selectedTerm, setSelectedTerm] = useState(null);   // 단어장과 매칭된 단어들

    async function fetchDetails() {
        try {
            setLoading(true);
            const res = await api.get(`/api/todayNews/${id}`);
            setSummaryText(res.data.summaryText);
            setSummaryTitle(res.data.summaryTitle);
            setCreatedAt(res.data.createdAt);
        } catch(e) {
            toast.error(e?.response?.data?.message || "상세 페이지 불러오기 실패.");
        }  finally {
            setLoading(false);
        }
    }

    async function fetchMyTerms() {
        try {
          const res = await api.get("/api/me/terms");
          const data = res.data;
          setMyTerms(Array.isArray(data) ? data : []);
        } catch(err) {
          toast.error(err?.response?.data?.message || "단어장 불러오기 실패");
        }
    }

    const matchedTerms = myTerms.filter((item) => summaryText.includes(item.term));  // 단어 포함하는지 검사, 배열로추가
    const words = matchedTerms
    .map((item) => item.term)   // term값만 뽑아냄
    .sort((a, b) => b.length - a.length);    // 긴 단어부터 정렬

    const regex = new RegExp(`(${words.join("|")})`, "g");  // 문자열 여러개를 한번에 찾기 위한 정규식 객체 생성 코드 "청년|주택|금리"
    const parts = summaryText.split(regex);   // regex 단어 기준으로 잘라서 배열로 생성

    //console.log(parts);

    useEffect(()=> {
      if (!id) return;
        fetchDetails();
        fetchMyTerms();
    }, [id])

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
        <p className="summary-detail-date">
          {createdAt ? new Date(createdAt).toLocaleDateString("ko-KR", 
            { year: "numeric", month: "2-digit", day: "2-digit", hour: "2-digit",minute: "2-digit",}) : ""}
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