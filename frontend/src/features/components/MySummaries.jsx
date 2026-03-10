import { useEffect, useState } from "react";
import api from "../../api"
import toast from "react-hot-toast";

export default function MySummaries() {

    const [summaries, setSummaries] = useState([]);

    async function fetchMySummaries() {
        try {
            const res = await api.get("/api/me/summaries");
            const data = res.data;
            setSummaries(Array.isArray(data) ? data : []);    // 배열있는지 검증
        } catch (err) {
            toast.error(err?.response?.data?.message || "불러오기 실패");
        }
    }

    useEffect(()=> {
        fetchMySummaries();
    }, []);

    return (
        <>
          {summaries.map((p) => (
            <div key={p.id}>
            <p>{p.summaryText}</p>
            <button>공유하기</button>
            </div>
         ))}
        </>
    )
}