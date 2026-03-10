import { useEffect, useState } from "react";
import api from "../../api"
import toast from "react-hot-toast";

export default function MyTerms() {

    const [terms, setTerms] = useState([]);
    
    const [term, setTerm] = useState("");
    const [definition, setDefinition] = useState("");

    async function fetchMyTerms() {
        try {
            const res = await api.get("/api/me/terms");
            const data = res.data;
            setTerms(Array.isArray(data) ? data : []);    // 배열있는지 검증
            setTerm("");
            setDefinition("");
        } catch (err) {
            toast.error(err?.response?.data?.message || "불러오기 실패");
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
        fetchMyTerms();
    }, []);

    return (
        <>
          {terms.map((t) => (
            <div key={t.id}>
            <p>{t.term}: {t.definition}</p>
            <button>수정</button> <button>삭제</button>
            </div>
         ))}

            <form onSubmit={postMyTerm}>
                <input placeholder="단어" value={term} onChange={(e)=>setTerm(e.target.value)}></input>
                <input placeholder="정의" value={definition} onChange={(e)=>setDefinition(e.target.value)}></input>
                <button type="submit">사전에 등록</button>
            </form>
        </>

    )
}