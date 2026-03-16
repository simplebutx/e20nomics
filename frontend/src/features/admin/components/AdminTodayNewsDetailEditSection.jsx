export default function AdminTodayNewsDetailEditSection({summaryTitle,summaryText,isPublished,createdAt,saving,deleting,
  setSummaryTitle,setSummaryText,onUpdate,onDelete,onTogglePublished,onGoList,formatDate,
}) {
  return (
    <div className="admin-today-news-detail-card">
      <div className="admin-today-news-detail-top">
        <h1 className="admin-today-news-detail-title">오늘의 뉴스 상세</h1>
        <span
          className={
            isPublished
              ? "admin-today-news-status published"
              : "admin-today-news-status unpublished"
          }
        >
          {isPublished ? "게시됨" : "미게시"}
        </span>
      </div>

      <p className="admin-today-news-detail-date">
        등록일: {formatDate(createdAt)}
      </p>

      <div className="admin-today-news-detail-field">
        <label className="admin-today-news-detail-label">제목</label>
        <input
          type="text"
          value={summaryTitle}
          onChange={(e) => setSummaryTitle(e.target.value)}
          className="admin-today-news-detail-input"
          placeholder="제목을 입력하세요"
        />
      </div>

      <div className="admin-today-news-detail-field">
        <label className="admin-today-news-detail-label">요약문</label>
        <textarea
          value={summaryText}
          onChange={(e) => setSummaryText(e.target.value)}
          className="admin-today-news-detail-textarea"
          placeholder="요약문을 입력하세요"
        />
      </div>

      <div className="admin-today-news-detail-buttons">
        <button
          type="button"
          onClick={onUpdate}
          disabled={saving}
          className="admin-today-news-detail-btn edit"
        >
          {saving ? "수정 중..." : "수정하기"}
        </button>

        <button
          type="button"
          onClick={onDelete}
          disabled={deleting}
          className="admin-today-news-detail-btn delete"
        >
          {deleting ? "삭제 중..." : "삭제하기"}
        </button>

        <button
          type="button"
          onClick={onGoList}
          className="admin-today-news-detail-btn list"
        >
          목록으로
        </button>

        <button
          type="button"
          onClick={onTogglePublished}
          className="admin-today-news-detail-btn publish"
        >
          {isPublished ? "게시 취소로 변경" : "공개 게시로 변경"}
        </button>
      </div>
    </div>
  );
}