import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "@/api";
import handleApiError from "@/shared/utils/handleApiError";
import "@/features/mypage/css/MySummaries.css";

export default function MySummaries() {

    const [summaries, setSummaries] = useState([]);
    const [loading, setLoading] = useState(true);

    async function fetchMySummaries() {
        try {
            setLoading(true);
            const res = await api.get("/api/me/summaries");
            const data = res.data;
            setSummaries(Array.isArray(data) ? data : []);    // 배열있는지 검증
        } catch (e) {
            handleApiError(e, "페이지 불러오기 실패");
        } finally {
            setLoading(false);
        }
    }

    useEffect(()=> {
       fetchMySummaries();
    }, []);

    return (
    <div className="my-summaries-page">
      <div className="my-summaries-container">
        <header className="my-summaries-header">
          <p className="my-summaries-label">My Library</p>
          <h1>내 기사</h1>
          <p className="my-summaries-desc">
            내가 저장한 기사 요약을 한 곳에서 관리해보세요.
          </p>
        </header>

        {loading ? (
          <div className="my-summaries-state-card">
            <p>불러오는 중...</p>
          </div>
        ) : summaries.length === 0 ? (
          <div className="my-summaries-state-card">
            <p>저장한 기사가 아직 없습니다.</p>
          </div>
        ) : (
          <section className="my-summaries-feed">
            {summaries.map((p, index) => (
              <article className="my-summary-card" key={p.id}>
                <Link to={`/summaries/${p.id}`}>
                <div className="my-summary-card-top">
                  <span className="my-summary-order">{String(index + 1).padStart(2, "0")}</span>
                </div>

                <h2 className="my-summary-title">{p.summaryTitle || `내 기사 ${index + 1}`}</h2>

                <p className="my-summary-text">{p.summaryText}</p>

                <div className="my-summary-card-bottom">
                  <span className="my-summary-date">
                    {p.createdAt
                      ? new Date(p.createdAt).toLocaleDateString ("ko-KR", { year: "numeric", month: "2-digit", day: "2-digit", hour: "2-digit",minute: "2-digit",})
                      : "저장한 기사"}
                  </span>
                </div>
                </Link>
              </article>
            ))}
          </section>
        )}
      </div>
    </div>
  );
}
