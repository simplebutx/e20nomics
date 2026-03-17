export default function TermDefinitionCard({
  selectedTerm,
  isAlreadySaved,
  onSaveMyTerm,
}) {
  return (
    <div className={`term-definition-card ${selectedTerm.source}`}>
      <h3>{selectedTerm.term}</h3>

      {selectedTerm.source === "linked" && (
        <>
          <p>{selectedTerm.definition}</p>
          <small className="term-source-label">출처 · 오늘의 뉴스</small>

          <div className="term-card-action">
            <button
              type="button"
              className="btn btn-secondary term-save-button"
              onClick={onSaveMyTerm}
              disabled={isAlreadySaved}
            >
              {isAlreadySaved ? "이미 저장됨" : "내 단어장에 저장"}
            </button>
          </div>
        </>
      )}

      {selectedTerm.source === "my" && (
        <>
          <p>{selectedTerm.definition}</p>
          <small className="term-source-label">출처 · 내 단어장</small>
        </>
      )}

      {selectedTerm.source === "both" && (
        <>
          <p>
            <strong>오늘의 뉴스 단어 뜻:</strong> {selectedTerm.definition}
          </p>
          <p>
            <strong>내 단어장 뜻:</strong> {selectedTerm.myDefinition}
          </p>
          <small className="term-source-label">
            출처 · 오늘의 뉴스 + 내 단어장
          </small>
        </>
      )}
    </div>
  );
}