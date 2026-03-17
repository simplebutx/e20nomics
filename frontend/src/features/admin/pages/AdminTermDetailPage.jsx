import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "@/api";
import toast from "react-hot-toast";
import "@/features/admin/css/AdminTermPage.css";
import "@/shared/css/Button.css";

export default function AdminTermDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [term, setTerm] = useState("");
  const [definition, setDefinition] = useState("");
  const [createdAt, setCreatedAt] = useState("");
  const [updatedAt, setUpdatedAt] = useState("");

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [deleting, setDeleting] = useState(false);

  async function fetchTermDetail() {
    try {
      setLoading(true);
      const res = await api.get(`/api/admin/terms/${id}`);

      setTerm(res.data.term || "");
      setDefinition(res.data.definition || "");
      setCreatedAt(res.data.createdAt || "");
      setUpdatedAt(res.data.updatedAt || "");
    } catch (err) {
      toast.error(err?.response?.data?.message || "단어 상세 조회에 실패했습니다.");
      navigate("/admin/terms");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    fetchTermDetail();
  }, [id]);

  async function handleUpdate() {
    if (saving) return;

    if (!term.trim() || !definition.trim()) {
      toast.error("단어와 정의를 입력해 주세요.");
      return;
    }

    try {
      setSaving(true);

      await api.put(`/api/admin/terms/${id}`, {
        term: term.trim(),
        definition: definition.trim(),
      });

      toast.success("단어가 수정되었습니다.");
      await fetchTermDetail();
    } catch (err) {
      toast.error(err?.response?.data?.message || "단어 수정에 실패했습니다.");
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete() {
    if (deleting) return;

    const ok = window.confirm("정말 삭제하시겠습니까?");
    if (!ok) return;

    try {
      setDeleting(true);
      await api.delete(`/api/admin/terms/${id}`);
      toast.success("단어가 삭제되었습니다.");
      navigate("/admin/terms");
    } catch (err) {
      toast.error(err?.response?.data?.message || "단어 삭제에 실패했습니다.");
    } finally {
      setDeleting(false);
    }
  }

  function formatDate(dateString) {
    if (!dateString) return "";
    return new Date(dateString).toLocaleString("ko-KR");
  }

  if (loading) {
    return (
      <div className="admin-term-page">
        <div className="admin-term-container">
          <div className="admin-term-detail-card">
            <p className="admin-term-loading">불러오는 중...</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="admin-term-page">
      <div className="admin-term-container">
        <div className="admin-term-detail-card">
          <div className="admin-term-detail-top">
            <h1 className="admin-term-detail-title">관리자 단어 상세</h1>
          </div>

          <div className="admin-term-detail-dates">
            <p>등록일: {formatDate(createdAt)}</p>
            <p>수정일: {formatDate(updatedAt)}</p>
          </div>

          <div className="admin-term-detail-field">
            <label className="admin-term-detail-label">단어</label>
            <input
              type="text"
              value={term}
              onChange={(e) => setTerm(e.target.value)}
              className="admin-term-detail-input"
              placeholder="단어를 입력하세요"
            />
          </div>

          <div className="admin-term-detail-field">
            <label className="admin-term-detail-label">정의</label>
            <textarea
              value={definition}
              onChange={(e) => setDefinition(e.target.value)}
              className="admin-term-detail-textarea"
              placeholder="정의를 입력하세요"
            />
          </div>

          <div className="admin-term-detail-actions">
            <button
              type="button"
              onClick={handleUpdate}
              disabled={saving}
              className="btn btn-primary"
            >
              {saving ? "수정 중..." : "수정하기"}
            </button>

            <button
              type="button"
              onClick={handleDelete}
              disabled={deleting}
              className="btn btn-danger"
            >
              {deleting ? "삭제 중..." : "삭제하기"}
            </button>

            <button
              type="button"
              onClick={() => navigate("/admin/terms")}
              className="btn btn-secondary"
            >
              목록으로
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}