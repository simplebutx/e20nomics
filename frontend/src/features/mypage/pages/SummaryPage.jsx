import { use, useState } from "react";
import api from "@/api";
import toast from "react-hot-toast";
import "@/features/mypage/css/SummaryPage.css";

export default function SummaryPage() {
  const [text, setText] = useState("");
  const [summaryTitle, setSummaryTitle] = useState("");
  const [summaryText, setSummaryText] = useState("");
  
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
      const res = await api.post("/api/summaries/generate", { text });
      setSummaryTitle(res.data.summaryTitle);
      setSummaryText(res.data.summaryText);
      setCanSave(res.data.canSave);
    } catch (e) {
      toast.error(e?.response?.data?.message || "요약에 실패했습니다.");
      setCanSave(false);
    } finally {
      setLoading(false);
    }
  }

  async function save() {
    if (!summaryText.trim()) {
      toast.error("저장할 요약 내용이 없습니다.");
      return;
    }

    try {
      await api.post("/api/summaries", {
        originalText: text,
        summaryTitle: summaryTitle,
        summaryText: summaryText,
      });
      toast.success("저장 완료");
    } catch (err) {
      toast.error(err?.response?.data?.message || "저장 실패");
    }
  }

  function resetAll() {
    setSummaryText("");
    setSummaryTitle("");
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
    <div className="summary-page">
      <div className="summary-container">
        <header className="summary-header">
          <p className="summary-label">AI Summary</p>
          <h1>기사 요약</h1>
          <p className="summary-desc">
            긴 경제 기사를 붙여넣으면 핵심만 빠르게 정리해드려요.
          </p>
        </header>

        <section className="summary-input-card">
          <div className="section-top">
            <h2>기사 입력</h2>
            <span className="text-count">{text.length}자</span>
          </div>

          <textarea
            className="summary-textarea"
            rows={14}
            value={text}
            onChange={(e) => setText(e.target.value)}
            placeholder="경제 뉴스 기사 본문을 붙여넣어 주세요."
          />

          <div className="summary-input-actions">
            <button className="primary-btn" onClick={submit} disabled={loading} type="button">
              {loading ? "요약 중..." : "요약하기"}
            </button>

            <button className="ghost-btn" onClick={resetAll} type="button" disabled={loading && !text}>
              초기화
            </button>
          </div>
        </section>

        <section className="summary-result-card">
          <div className="section-top">
            <h2>요약 결과</h2>
            {summaryTitle && <h3>{summaryTitle}</h3>}
            {summaryText && <span className="result-badge">완료</span>}
          </div>

          {summaryText ? (
            <>
              <div className="summary-result-box">
                <p>제목: {summaryTitle}</p>
                <p>{summaryText}</p>
              </div>

              {canSave && (
                <div className="summary-result-actions">
                  <button className="primary-btn" onClick={save} type="button">저장</button>
                  <button className="outline-btn" onClick={retrySummary} type="button" disabled={loading}>다시 요약</button>
                  <button className="ghost-btn" onClick={resetAll} type="button">새로 입력</button>
                </div>
              )}
            </>
          ) : (
            <div className="summary-empty-box">
              <p>아직 생성된 요약이 없습니다.</p>
              <span>기사 내용을 입력한 뒤 요약하기를 눌러보세요.</span>
            </div>
          )}
        </section>
      </div>
    </div>
  );
}