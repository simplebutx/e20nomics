import { useEffect, useState } from "react";
import api from "../../api";
import toast from "react-hot-toast";
import { Link } from "react-router-dom";
import "@/features/css/AdminTodayNewsList.css";

export default function AdminTodayNewsList() {
  const [announcements, setAnnouncements] = useState([]);
  const [loading, setLoading] = useState(true);

  async function fetchAnnouncements() {
    try {
      setLoading(true);
      const res = await api.get("/api/announcements");
      const data = res.data;
      setAnnouncements(Array.isArray(data) ? data : []);
    } catch (err) {
      toast.error(err?.response?.data?.message || "불러오기 실패");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    fetchAnnouncements();
  }, []);

  return (
    <div className="admin-news-list-page">
      <div className="admin-news-list-container">
        <div className="admin-news-list-header">
          <h2 className="admin-news-list-title">오늘의 뉴스 관리</h2>
          <p className="admin-news-list-count">{announcements.length}개</p>
        </div>

        {loading ? (
          <div className="admin-news-list-state">
            <p>불러오는 중...</p>
          </div>
        ) : announcements.length === 0 ? (
          <div className="admin-news-list-state">
            <p>등록된 뉴스가 없습니다.</p>
          </div>
        ) : (
          <div className="admin-news-list-box">
            {announcements.map((a) => (
              <Link
                key={a.id}
                to={`/today/${a.id}`}
                className="admin-news-list-item"
              >
                <div className="admin-news-list-main">
                  <p className="admin-news-list-item-title">
                    {a.summaryTitle || "제목 없음"}
                  </p>
                </div>

                <div className="admin-news-list-side">
                  <span className="admin-news-list-date">
                    {a.createdAt
                      ? new Date(a.createdAt).toLocaleString("ko-KR", {
                          year: "numeric",
                          month: "2-digit",
                          day: "2-digit",
                          hour: "2-digit",
                          minute: "2-digit",
                          hour12: false,
                        })
                      : "날짜 없음"}
                  </span>
                </div>
              </Link>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}