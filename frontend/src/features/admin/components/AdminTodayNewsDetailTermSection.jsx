import { Link } from "react-router-dom";

export default function AdminTodayNewsDetailTermSection({
  linkedTerms,
  availableTerms,
  selectedTermId,
  setSelectedTermId,
  termLoading,
  linking,
  unlinkingId,
  onLinkTerm,
  onUnlinkTerm,
}) {
  return (
    <div className="admin-today-news-term-card">
      <div className="admin-today-news-term-top">
        <div>
          <h2 className="admin-today-news-term-title">연결된 관리자 단어</h2>
          <p className="admin-today-news-term-subtitle">
            오늘의 뉴스 상세에 보여줄 단어를 연결할 수 있습니다.
          </p>
        </div>

        <Link to="/admin/terms" className="btn btn-outline">
          단어장 관리
        </Link>
      </div>

      <div className="admin-today-news-term-link-box">
        <select
          value={selectedTermId}
          onChange={(e) => setSelectedTermId(e.target.value)}
          className="admin-today-news-term-select"
        >
          <option value="">연결할 단어를 선택하세요</option>
          {availableTerms.map((term) => (
            <option key={term.id} value={term.id}>
              {term.term}
            </option>
          ))}
        </select>

        <button
          type="button"
          onClick={onLinkTerm}
          disabled={linking || availableTerms.length === 0}
          className="btn btn-primary"
        >
          {linking ? "연결 중..." : "단어 연결"}
        </button>
      </div>

      {availableTerms.length === 0 && (
        <p className="admin-today-news-term-guide">
          연결 가능한 단어가 없습니다. 이미 모두 연결되었거나 관리자 단어장이 비어
          있습니다.
        </p>
      )}

      <div className="admin-today-news-term-list-wrap">
        <h3 className="admin-today-news-term-list-title">
          현재 연결된 단어 ({linkedTerms.length})
        </h3>

        {termLoading ? (
          <p className="admin-today-news-term-empty">불러오는 중...</p>
        ) : linkedTerms.length === 0 ? (
          <p className="admin-today-news-term-empty">
            아직 연결된 단어가 없습니다.
          </p>
        ) : (
          <div className="admin-today-news-term-list">
            {linkedTerms.map((term) => (
              <div key={term.id} className="admin-today-news-term-item">
                <div className="admin-today-news-term-item-content">
                  <p className="admin-today-news-term-word">{term.term}</p>
                  <p className="admin-today-news-term-definition">
                    {term.definition}
                  </p>
                </div>

                <button
                  type="button"
                  onClick={() => onUnlinkTerm(term.id)}
                  disabled={unlinkingId === term.id}
                  className="btn btn-danger"
                >
                  {unlinkingId === term.id ? "해제 중..." : "연결 해제"}
                </button>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}