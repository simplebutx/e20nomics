import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "@/api";
import toast from "react-hot-toast";
import "@/features/mypage/css/MyTerms.css";

export default function MyTerms() {

    const [terms, setTerms] = useState([]);
    const [term, setTerm] = useState("");
    const [definition, setDefinition] = useState("");
    const [loading, setLoading] = useState(true);
    const nav = useNavigate();

    async function fetchMyTerms() {
        try {
            setLoading(true);
            const res = await api.get("/api/me/terms");
            const data = res.data;
            setTerms(Array.isArray(data) ? data : []);    // 배열있는지 검증
            setTerm("");
            setDefinition("");
        } catch (err) {
            toast.error(err?.response?.data?.message || "불러오기 실패");
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
        const form = {term, definition};
        try {
            await api.post("/api/me/terms", form);
            toast.success("단어를 등록했습니다.");
                fetchMyTerms();
        } catch(err) {
            const message = err?.response?.data?.message || "단어 등록 실패";
            toast.error(message);
        }
    }

    useEffect(()=> {
      const token = localStorage.getItem("accessToken");
      if (!token) {
        toast("로그인 후 이용 가능합니다.");
        nav("/login");
      }
      else fetchMyTerms();
    }, []);

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

          <form className="term-form" onSubmit={postMyTerm}>
            <input className="term-input" placeholder="단어" value={term} onChange={(e) => setTerm(e.target.value)}/>
            <textarea className="term-textarea" placeholder="정의" value={definition} onChange={(e) => setDefinition(e.target.value)} rows={4}/>
            <button className="term-submit-btn" type="submit">사전에 등록</button>
          </form>
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