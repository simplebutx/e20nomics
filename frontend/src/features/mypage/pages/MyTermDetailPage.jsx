import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "@/api";
import toast from "react-hot-toast";
import "@/features/mypage/css/MyTermDetailPage.css";
import "@/shared/css/button.css";

export default function MyTermDetailPage() {
  const { id } = useParams();
  const nav = useNavigate();

  const [term, setTerm] = useState("");
  const [definition, setDefinition] = useState("");
  const [createdAt, setCreatedAt] = useState("");
  const [loading, setLoading] = useState(false);
  const [deleting, setDeleting] = useState(false);

  async function fetchTermDetail() {
    try {
      setLoading(true);
      const res = await api.get(`/api/me/terms/${id}`);
      setTerm(res.data.term);
      setDefinition(res.data.definition);
      setCreatedAt(res.data.createdAt);
    } catch (err) {
      toast.error(err?.response?.data?.message || "단어 상세 조회 실패");
    } finally {
      setLoading(false);
    }
  }

  async function deleteMyTerm() {
    const ok = window.confirm("이 단어를 삭제하시겠습니까?");
    if (!ok || deleting) return;

    try {
      setDeleting(true);
      await api.delete(`/api/me/terms/${id}`);
      toast.success("단어를 삭제했습니다.");
      nav("/terms");
    } catch (err) {
      toast.error(err?.response?.data?.message || "단어 삭제 실패");
    } finally {
      setDeleting(false);
    }
  }

  useEffect(() => {
    fetchTermDetail();
  }, [id]);

  if (loading) {
    return <div className="my-term-detail-loading">불러오는 중...</div>;
  }

  return (
    <div className="my-term-detail-page">
      <div className="my-term-detail-card">
        <div className="my-term-detail-header">
          <span className="my-term-detail-badge">내 단어장</span>
          <h1 className="my-term-detail-title">{term}</h1>
          <p className="my-term-detail-date">
            등록일:{" "}
            {createdAt
              ? new Date(createdAt).toLocaleString("ko-KR", {
                  year: "numeric",
                  month: "2-digit",
                  day: "2-digit",
                  hour: "2-digit",
                  minute: "2-digit",
                })
              : "-"}
          </p>
        </div>

        <div className="my-term-detail-section">
          <h2 className="my-term-detail-section-title">뜻</h2>
          <p className="my-term-detail-definition">{definition}</p>
        </div>

        <div className="my-term-detail-actions">
          <button
            type="button"
            className="btn btn-outline"
            onClick={() => nav(`/terms/${id}/edit`)}
          >
            수정
          </button>

          <button
            type="button"
            className="btn btn-danger"
            onClick={deleteMyTerm}
            disabled={deleting}
          >
            {deleting ? "삭제 중..." : "삭제"}
          </button>
        </div>
      </div>
    </div>
  );
}