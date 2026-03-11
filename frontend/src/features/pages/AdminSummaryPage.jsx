import { useState } from "react";
import api from "../../api";
import toast from "react-hot-toast";
import "@/features/css/AdminSummaryPage.css";

export default function AdminSummaryPage() {
  const [text, setText] = useState("");
  const [summary, setSummary] = useState("");
  const [loading, setLoading] = useState(false);
  const [canSave, setCanSave] = useState(false);

  async function submit() {
    if (loading) return;

    if (!text.trim()) {
      toast.error("기사 내용을 입력해 주세요.");
      return;
    }

    try {
      setLoading(true);
      const res = await api.post("/api/admin/announcements/generate", { text });
      setSummary(res.data.summary);
      setCanSave(res.data.canSave);
    } catch (e) {
      toast.error(e?.response?.data?.message || "요약에 실패했습니다.");
      setCanSave(false);
    } finally {
      setLoading(false);
    }
  }

  async function save() {
    if (!summary.trim()) {
      toast.error("저장할 요약 내용이 없습니다.");
      return;
    }

    try {
      await api.post("/api/admin/announcements", {
        originalText: text,
        summaryText: summary,
        isPublic: true,
      });
      toast.success("오늘의 뉴스가 등록되었습니다.");
    } catch (err) {
      toast.error(err?.response?.data?.message || "저장 실패");
    }
  }

  function resetAll() {
    setText("");
    setSummary("");
    setCanSave(false);
  }

  function retrySummary() {
    if (!text.trim()) {
      toast.error("기사 내용을 먼저 입력해 주세요.");
      return;
    }
    submit();
  }

  return (
    <div className="admin-summary-page">
      <div className="admin-summary-container">
        <header className="admin-summary-header">
          <p className="admin-summary-label">Admin Summary</p>
          <h1>오늘의 뉴스 등록</h1>
          <p className="admin-summary-desc">
            경제 기사를 요약해 오늘의 뉴스 브리핑으로 등록합니다.
          </p>
        </header>

        <section className="admin-summary-input-card">
          <div className="admin-summary-section-top">
            <h2>기사 입력</h2>
            <span className="admin-summary-text-count">{text.length}자</span>
          </div>

          <textarea
            className="admin-summary-textarea"
            rows={14}
            value={text}
            onChange={(e) => setText(e.target.value)}
            placeholder="오늘의 뉴스로 등록할 경제 기사 본문을 붙여넣어 주세요."
          />

          <div className="admin-summary-input-actions">
            <button
              className="admin-primary-btn"
              onClick={submit}
              disabled={loading}
              type="button"
            >
              {loading ? "요약 중..." : "요약하기"}
            </button>

            <button
              className="admin-ghost-btn"
              onClick={resetAll}
              type="button"
            >
              초기화
            </button>
          </div>
        </section>

        <section className="admin-summary-result-card">
          <div className="admin-summary-section-top">
            <h2>요약 결과</h2>
            {summary && <span className="admin-summary-result-badge">완료</span>}
          </div>

          {summary ? (
            <>
              <div className="admin-summary-result-box">
                <p>{summary}</p>
              </div>

              {canSave && (
                <div className="admin-summary-result-actions">
                  <button
                    className="admin-primary-btn"
                    onClick={save}
                    type="button"
                  >
                    오늘의 뉴스 저장
                  </button>
                  <button
                    className="admin-outline-btn"
                    onClick={retrySummary}
                    type="button"
                    disabled={loading}
                  >
                    다시 요약
                  </button>
                  <button
                    className="admin-ghost-btn"
                    onClick={resetAll}
                    type="button"
                  >
                    새로 입력
                  </button>
                </div>
              )}
            </>
          ) : (
            <div className="admin-summary-empty-box">
              <p>아직 생성된 요약이 없습니다.</p>
              <span>기사 내용을 입력한 뒤 요약하기를 눌러보세요.</span>
            </div>
          )}
        </section>
      </div>
    </div>
  );
}