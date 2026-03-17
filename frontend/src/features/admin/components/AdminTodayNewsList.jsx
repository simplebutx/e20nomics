import { useEffect, useMemo, useState } from "react";
import api from "@/api";
import toast from "react-hot-toast";
import { Link } from "react-router-dom";
import "@/features/admin/css/AdminTodayNewsList.css";
import "@/shared/css/button.css";

export default function AdminTodayNewsList() {
  const [announcements, setAnnouncements] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState("all"); // all | published | unpublished

  async function fetchAnnouncements() {
    try {
      setLoading(true);
      const res = await api.get("/api/admin/todayNews");
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

  const filteredAnnouncements = useMemo(() => {
    if (filter === "published") {
      return announcements.filter((item) => item.isPublished === true);
    }
    if (filter === "unpublished") {
      return announcements.filter((item) => item.isPublished === false);
    }
    return announcements;
  }, [announcements, filter]);

  function formatDate(date) {
    if (!date) return "날짜 없음";
    return new Date(date).toLocaleString("ko-KR", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
      hour12: false,
    });
  }

  function getFilterButtonClass(type) {
    return filter === type ? "btn btn-primary" : "btn btn-outline";
  }

  return (
    <div className="admin-news-list-page">
      <div className="admin-news-list-container">
        <div className="admin-news-list-header">
          <div>
            <h2 className="admin-news-list-title">오늘의 뉴스 관리</h2>
            <p className="admin-news-list-count">
              {filteredAnnouncements.length}개
            </p>
          </div>
        </div>

        <div className="admin-news-list-filters">
          <button
            type="button"
            className={getFilterButtonClass("all")}
            onClick={() => setFilter("all")}
          >
            전체
          </button>

          <button
            type="button"
            className={getFilterButtonClass("published")}
            onClick={() => setFilter("published")}
          >
            공개 게시물
          </button>

          <button
            type="button"
            className={getFilterButtonClass("unpublished")}
            onClick={() => setFilter("unpublished")}
          >
            비공개 게시물
          </button>
        </div>

        {loading ? (
          <div className="admin-news-list-state">
            <p>불러오는 중...</p>
          </div>
        ) : filteredAnnouncements.length === 0 ? (
          <div className="admin-news-list-state">
            <p>해당하는 뉴스가 없습니다.</p>
          </div>
        ) : (
          <div className="admin-news-list-box">
            {filteredAnnouncements.map((a) => (
              <Link
                key={a.id}
                to={`/admin/todayNews/${a.id}`}
                className="admin-news-list-item"
              >
                <div className="admin-news-list-main">
                  <p className="admin-news-list-item-title">
                    {a.summaryTitle || "제목 없음"}
                  </p>
                </div>

                <div className="admin-news-list-side">
                  <span
                    className={
                      a.isPublished
                        ? "admin-news-list-badge published"
                        : "admin-news-list-badge unpublished"
                    }
                  >
                    {a.isPublished ? "공개" : "비공개"}
                  </span>

                  <span className="admin-news-list-date">
                    {formatDate(a.createdAt)}
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