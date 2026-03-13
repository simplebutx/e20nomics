import { useEffect, useState } from "react";
import api from "../../api"
import toast from "react-hot-toast";
import "@/features/css/TodayNewsPage.css";

export default function TodayNewsPage() {

    const [announcements, setAnnouncements] = useState([]);
    const [loading, setLoading] = useState(true);

    async function fetchAnnouncements() {
        try { 
        setLoading(true);
        const res = await api.get("/api/announcements");
        const data = res.data;
        setAnnouncements(Array.isArray(data) ? data : []);
        } catch(err) {
            toast.error(err?.response?.data?.message || "불러오기 실패");
        } finally {
            setLoading(false);
        }
    }

    useEffect(()=> {
        fetchAnnouncements();
    }, [])


    return (
    <div className="today-news-page">
      <div className="today-news-container">
        <header className="today-news-header">
          <p className="today-news-label">Daily Brief</p>
          <h1>오늘의 뉴스</h1>
          <p className="today-news-desc">
            오늘의 주요 경제 이슈를 짧고 쉽게 정리했어요.
          </p>
        </header>

        {loading ? (
          <div className="today-news-state-card">
            <p>불러오는 중...</p>
          </div>
        ) : announcements.length === 0 ? (
          <div className="today-news-state-card">
            <p>등록된 오늘의 뉴스가 아직 없습니다.</p>
          </div>
        ) : (
          <section className="news-feed">
            {announcements.map((a, index) => (
              <article className="news-card" key={a.id}>
                <div className="news-card-top">
                  <span className="news-badge">주요 뉴스</span>
                  <span className="news-order">
                    {String(index + 1).padStart(2, "0")}
                  </span>
                </div>

                <h2 className="news-title">{a.summaryTitle || `오늘의 경제 뉴스 ${index + 1}`}</h2>

                <p className="news-summary">{a.summaryText}</p>

                <div className="news-card-bottom">
                  <span className="news-meta">
                    {a.createdAt
                      ? new Date(a.createdAt).toLocaleDateString("ko-KR")
                      : "오늘 업데이트"}
                  </span>
                </div>
              </article>
            ))}
          </section>
        )}
      </div>
    </div>
  );
}