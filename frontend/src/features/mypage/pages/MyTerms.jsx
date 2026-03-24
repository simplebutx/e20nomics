import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "@/api";
import toast from "react-hot-toast";
import handleApiError from "@/shared/utils/handleApiError";
import "@/features/mypage/css/MyTerms.css";
import "@/shared/css/Button.css";

export default function MyTerms() {
  const [terms, setTerms] = useState([]);

  const [activeTab, setActiveTab] = useState("ai"); // "ai" | "manual"

  const [term, setTerm] = useState("");
  const [definition, setDefinition] = useState("");

  const [aiTerm, setAiTerm] = useState("");
  const [aiResult, setAiResult] = useState("");
  const [aiCanSave, setAiCanSave] = useState(false);
  const [aiLoading, setAiLoading] = useState(false);
  const [aiSaving, setAiSaving] = useState(false);

  const [loading, setLoading] = useState(true);
  const nav = useNavigate();

  async function fetchMyTerms() {
    try {
      setLoading(true);
      const res = await api.get("/api/me/terms");
      const data = res.data;
      setTerms(Array.isArray(data) ? data : []);
    } catch (e) {
      handleApiError(e, "페이지 불러오기 실패");
    } finally {
      setLoading(false);
    }
  }

  async function postMyTerm(e) {
    e.preventDefault();

    if (!term.trim() || !definition.trim()) {
      toast.error("모든 항목을 입력해 주세요.");
      return;
    }

    try {
      await api.post("/api/me/terms", {
        term: term.trim(),
        definition: definition.trim(),
      });

      toast.success("단어를 등록했습니다.");
      setTerm("");
      setDefinition("");
      fetchMyTerms();
    } catch (e) {
      handleApiError(e, "단어 등록 실패");
    }
  }

  async function createAiTerm(e) {
    e.preventDefault();

    if (!aiTerm.trim()) {
      toast.error("단어를 입력해 주세요.");
      return;
    }

    try {
      setAiLoading(true);
      setAiResult("");
      setAiCanSave(false);

      const res = await api.post("/api/me/terms/generate", {
        term: aiTerm.trim(),
      });

      const data = res.data;

      setAiResult(data.definition || "");
      setAiCanSave(!!data.canSave);

      if (data.canSave) {
        toast.success("AI 정의를 생성했습니다.");
      } else {
        handleApiError(e, "정의 생성 실패");
      }
    } catch (e) {
      handleApiError(e, "정의 생성 실패");
    } finally {
      setAiLoading(false);
    }
  }

  async function saveAiTerm() {
    if (!aiTerm.trim() || !aiResult.trim() || !aiCanSave) {
      toast.error("먼저 AI 정의를 생성해 주세요.");
      return;
    }

    try {
      setAiSaving(true);

      await api.post("/api/me/terms", {
        term: aiTerm.trim(),
        definition: aiResult.trim(),
      });

      toast.success("AI 단어를 저장했습니다.");

      setAiTerm("");
      setAiResult("");
      setAiCanSave(false);

      fetchMyTerms();
    } catch (e) {
      handleApiError(e, "저장 실패");
    } finally {
      setAiSaving(false);
    }
  }

  useEffect(() => {
      fetchMyTerms();
  }, []);

  function getTabButtonClass(tab) {
    return activeTab === tab ? "btn btn-primary" : "btn btn-outline";
  }

  return (
    <div className="my-terms-page">
      <div className="my-terms-container">
        <header className="my-terms-header">
          <p className="my-terms-label">My Dictionary</p>
          <h1>내 단어장</h1>
          <p className="my-terms-desc">
            자주 보는 경제 용어를 직접 저장하고 정리해보세요.
          </p>
        </header>

        <section className="term-form-card">
          <h2 className="term-form-title">새 단어 등록</h2>

          <div className="term-tab-row">
            <button
              type="button"
              className={getTabButtonClass("ai")}
              onClick={() => setActiveTab("ai")}
            >
              AI로 등록
            </button>
            <button
              type="button"
              className={getTabButtonClass("manual")}
              onClick={() => setActiveTab("manual")}
            >
              직접 등록
            </button>
          </div>

          {activeTab === "ai" && (
            <form className="term-form" onSubmit={createAiTerm}>
              <input
                className="term-input"
                placeholder="예: 금리"
                value={aiTerm}
                onChange={(e) => setAiTerm(e.target.value)}
              />

              <textarea
                className="term-textarea"
                placeholder="AI가 생성한 정의가 여기에 표시됩니다."
                value={aiResult}
                readOnly
                rows={4}
              />

              <div className="term-btn-row">
                <button
                  className="btn btn-primary"
                  type="submit"
                  disabled={aiLoading}
                >
                  {aiLoading ? "생성 중..." : "AI 생성"}
                </button>

                <button
                  className="btn btn-secondary"
                  type="button"
                  onClick={saveAiTerm}
                  disabled={!aiCanSave || aiSaving}
                >
                  {aiSaving ? "저장 중..." : "저장"}
                </button>
              </div>
            </form>
          )}

          {activeTab === "manual" && (
            <form className="term-form" onSubmit={postMyTerm}>
              <input
                className="term-input"
                placeholder="단어"
                value={term}
                onChange={(e) => setTerm(e.target.value)}
              />

              <textarea
                className="term-textarea"
                placeholder="정의"
                value={definition}
                onChange={(e) => setDefinition(e.target.value)}
                rows={4}
              />

              <button className="btn btn-primary" type="submit">
                사전에 등록
              </button>
            </form>
          )}
        </section>

        <section className="term-list-section">
          <div className="term-list-top">
            <h2 className="term-list-title">저장한 단어</h2>
            {!loading && terms.length > 0 && (
              <span className="term-count">{terms.length}개</span>
            )}
          </div>

          {loading ? (
            <div className="term-state-card">
              <p>불러오는 중...</p>
            </div>
          ) : terms.length === 0 ? (
            <div className="term-state-card">
              <p>아직 등록한 단어가 없습니다.</p>
            </div>
          ) : (
            <div className="term-simple-list">
              {terms.map((t) => (
                <div
                  className="term-simple-item"
                  key={t.id}
                  onClick={() => nav(`/terms/${t.id}`)}
                  role="button"
                  tabIndex={0}
                  onKeyDown={(e) => {
                    if (e.key === "Enter" || e.key === " ") {
                      nav(`/terms/${t.id}`);
                    }
                  }}
                >
                  <span className="term-simple-word">{t.term}</span>
                  <span className="term-simple-divider">|</span>
                  <span className="term-simple-definition">{t.definition}</span>
                </div>
              ))}
            </div>
          )}
        </section>
      </div>
    </div>
  );
}
