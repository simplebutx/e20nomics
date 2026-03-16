import { useState } from "react";
import api from "@/api";
import toast from "react-hot-toast";

export default function AdminTermCreate({ onCreated }) {
  const [term, setTerm] = useState("");
  const [definition, setDefinition] = useState("");
  const [saving, setSaving] = useState(false);

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
    } catch (err) {
      toast.error(err?.response?.data?.message || "단어 등록에 실패했습니다.");
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="admin-term-create-card">
      <h2 className="admin-term-create-title">관리자 단어 등록</h2>

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
            className="admin-term-create-btn"
          >
            {saving ? "등록 중..." : "단어 등록"}
          </button>
        </div>
      </form>
    </div>
  );
}