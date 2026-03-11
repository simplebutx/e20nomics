import { useEffect, useState } from "react";
import api from "../../api";
import toast from "react-hot-toast";
import "@/features/css/AdminTermList.css";

export default function AdminTermList() {
  const [terms, setTerms] = useState([]);
  const [term, setTerm] = useState("");
  const [definition, setDefinition] = useState("");

  async function fetchTermList() {
    try {
      const res = await api.get("/api/admin/terms");
      const data = res.data;
      setTerms(Array.isArray(data) ? data : []);
    } catch (err) {
      toast.error(err?.response?.data?.message || "불러오기 실패");
    }
  }

  async function postGlobalTerm(e) {
    e.preventDefault();

    if (!term.trim() || !definition.trim()) {
      toast.error("모든 항목을 입력해 주세요.");
      return;
    }

    const form = { term, definition };

    try {
      await api.post("/api/admin/terms", form);
      toast.success("단어를 등록했습니다.");
      setTerm("");
      setDefinition("");
      fetchTermList();
    } catch (err) {
      const message = err?.response?.data?.message || "단어 등록 실패";
      toast.error(message);
    }
  }

  useEffect(() => {
    fetchTermList();
  }, []);

  return (
    <div className="admin-term-wrap">
      <form className="admin-term-form" onSubmit={postGlobalTerm}>
        <input
          type="text"
          placeholder="단어"
          value={term}
          onChange={(e) => setTerm(e.target.value)}
        />
        <textarea
          placeholder="정의"
          value={definition}
          onChange={(e) => setDefinition(e.target.value)}
          rows={3}
        />
        <button type="submit">사전에 등록</button>
      </form>

      <div className="admin-term-list">
        {terms.map((t) => (
          <div className="admin-term-item" key={t.id}>
            <h3>{t.term}</h3>
            <p>{t.definition}</p>
          </div>
        ))}
      </div>
    </div>
  );
}