import { useNavigate, useParams } from "react-router-dom";
import "@/features/admin/css/AdminTodayNewsDetailPage.css";
import "@/shared/css/Button.css";
import AdminTodayNewsDetailEditSection from "@/features/admin/components/AdminTodayNewsDetailEditSection";
import AdminTodayNewsDetailTermSection from "@/features/admin/components/AdminTodayNewsDetailTermSection";
import AdminTodayNewsImageSection from "@/features/admin/components/AdminTodayNewsImageSection";

export default function AdminTodayNewsDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  if (!id) {
    return (
      <div className="admin-today-news-detail-page">
        <div className="admin-today-news-detail-card">
          <p className="admin-today-news-detail-loading">잘못된 접근입니다.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="admin-today-news-detail-page">
      <AdminTodayNewsDetailEditSection
        id={id}
        onGoList={() => navigate("/admin/todayNews")}
        onDeleted={() => navigate("/admin")}
      />

      <AdminTodayNewsImageSection id={id} />

      <AdminTodayNewsDetailTermSection id={id} />
    </div>
  );
}