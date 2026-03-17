import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "@/api";
import toast from "react-hot-toast";
import "@/features/mypage/css/MyTermEditPage.css";
import "@/shared/css/button.css";

export default function MyTermEditPage() {
  const { id } = useParams();
  const nav = useNavigate();

  const [term, setTerm] = useState("");
  const [definition, setDefinition] = useState("");
  const [loading, setLoading] = useState(false);
  const [fetching, setFetching] = useState(false);

  async function fetchMyTerm() {
    try {
      setFetching(true);
      const res = await api.get(`/api/me/terms/${id}`);
      setTerm(res.data.term || "");
      setDefinition(res.data.definition || "");
    } catch (err) {
      toast.error(err?.response?.data?.message || "단어 정보를 불러오지 못했습니다.");
    } finally {
      setFetching(false);
    }
  }

  async function updateMyTerm(e) {
    e.preventDefault();

    if (loading) return;

    if (!term.trim() || !definition.trim()) {
      toast.error("모든 항목을 입력해 주세요.");
      return;
    }

    try {
      setLoading(true);

      await api.put(`/api/me/terms/${id}`, {
        term,
        definition,
      });

      toast.success("단어를 수정했습니다.");
      nav("/terms");
    } catch (err) {
      toast.error(err?.response?.data?.message || "단어 수정 실패");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    fetchMyTerm();
  }, [id]);

  if (fetching) {
    return <div className="my-terms-loading">불러오는 중...</div>;
  }

  return (
    <div className="my-terms-page">
      <div className="my-terms-container">
        <div className="my-terms-header">
          <span className="my-terms-badge">My Dictionary</span>
          <h1>단어 수정</h1>
          <p>기존에 저장한 경제 용어를 수정해보세요.</p>
        </div>

        <form className="my-terms-form-card" onSubmit={updateMyTerm}>
          <h2>단어 수정</h2>

          <input
            type="text"
            placeholder="단어"
            value={term}
            onChange={(e) => setTerm(e.target.value)}
            className="my-terms-input"
          />

          <textarea
            placeholder="정의"
            value={definition}
            onChange={(e) => setDefinition(e.target.value)}
            className="my-terms-textarea"
          />

          <div className="my-terms-form-actions">
            <button
              type="submit"
              className="btn btn-primary"
              disabled={loading}
            >
              {loading ? "수정 중..." : "수정하기"}
            </button>

            <button
              type="button"
              className="btn btn-secondary"
              onClick={() => nav(-1)}
              disabled={loading}
            >
              취소
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}