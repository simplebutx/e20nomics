import { useState } from "react";
import api from "@/api";
import toast from "react-hot-toast";
import handleApiError from "@/shared/utils/handleApiError";

export default function AdminTermCreate({ onCreated }) {
  const [activeTab, setActiveTab] = useState("ai"); // "ai" | "manual"

  const [term, setTerm] = useState("");
  const [definition, setDefinition] = useState("");
  const [saving, setSaving] = useState(false);

  const [aiTerm, setAiTerm] = useState("");
  const [aiResult, setAiResult] = useState("");
  const [aiCanSave, setAiCanSave] = useState(false);
  const [aiLoading, setAiLoading] = useState(false);
  const [aiSaving, setAiSaving] = useState(false);

  async function handleCreate(e) {
    e.preventDefault();
    if (saving) return;

    if (!term.trim() || !definition.trim()) {
      toast.error("단어와 정의를 입력해 주세요.");
      return;
    }

    try {
      setSaving(true);

      await api.post("/api/admin/terms", {
        term: term.trim(),
        definition: definition.trim(),
      });

      toast.success("단어가 등록되었습니다.");
      setTerm("");
      setDefinition("");

      if (onCreated) onCreated();
    } catch (e) {
      handleApiError(e, "등록 실패");
    } finally {
      setSaving(false);
    }
  }

  async function handleAiGenerate(e) {
    e.preventDefault();
    if (aiLoading) return;

    if (!aiTerm.trim()) {
      toast.error("단어를 입력해 주세요.");
      return;
    }

    try {
      setAiLoading(true);
      setAiResult("");
      setAiCanSave(false);

      const res = await api.post("/api/admin/terms/generate", {
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

  async function handleAiSave() {
    if (aiSaving) return;

    if (!aiTerm.trim() || !aiResult.trim() || !aiCanSave) {
      toast.error("먼저 AI 정의를 생성해 주세요.");
      return;
    }

    try {
      setAiSaving(true);

      await api.post("/api/admin/terms", {
        term: aiTerm.trim(),
        definition: aiResult.trim(),
      });

      toast.success("AI 단어가 등록되었습니다.");

      setAiTerm("");
      setAiResult("");
      setAiCanSave(false);

      if (onCreated) onCreated();
    } catch (e) {
      handleApiError(e, "등록 실패");
    } finally {
      setAiSaving(false);
    }
  }

  function getTabButtonClass(tab) {
    return activeTab === tab ? "btn btn-primary" : "btn btn-outline";
  }

  return (
    <div className="admin-term-create-card">
      <h2 className="admin-term-create-title">관리자 단어 등록</h2>

      <div className="admin-term-tab-row">
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
        <form onSubmit={handleAiGenerate} className="admin-term-create-form">
          <div className="admin-term-create-field">
            <label className="admin-term-create-label">단어</label>
            <input
              type="text"
              value={aiTerm}
              onChange={(e) => setAiTerm(e.target.value)}
              className="admin-term-create-input"
              placeholder="예: 브렌트유"
            />
          </div>

          <div className="admin-term-create-field">
            <label className="admin-term-create-label">AI 생성 정의</label>
            <textarea
              value={aiResult}
              readOnly
              className="admin-term-create-textarea"
              placeholder="AI가 생성한 정의가 여기에 표시됩니다."
            />
          </div>

          <div className="admin-term-create-actions">
            <button
              type="submit"
              disabled={aiLoading}
              className="btn btn-primary"
            >
              {aiLoading ? "생성 중..." : "AI 생성"}
            </button>

            <button
              type="button"
              disabled={!aiCanSave || aiSaving}
              className="btn btn-secondary"
              onClick={handleAiSave}
            >
              {aiSaving ? "저장 중..." : "바로 저장"}
            </button>
          </div>
        </form>
      )}

      {activeTab === "manual" && (
        <form onSubmit={handleCreate} className="admin-term-create-form">
          <div className="admin-term-create-field">
            <label className="admin-term-create-label">단어</label>
            <input
              type="text"
              value={term}
              onChange={(e) => setTerm(e.target.value)}
              className="admin-term-create-input"
              placeholder="예: 브렌트유"
            />
          </div>

          <div className="admin-term-create-field">
            <label className="admin-term-create-label">정의</label>
            <textarea
              value={definition}
              onChange={(e) => setDefinition(e.target.value)}
              className="admin-term-create-textarea"
              placeholder="단어 정의를 입력하세요"
            />
          </div>

          <div className="admin-term-create-actions">
            <button
              type="submit"
              disabled={saving}
              className="btn btn-primary"
            >
              {saving ? "등록 중..." : "단어 등록"}
            </button>
          </div>
        </form>
      )}
    </div>
  );
}