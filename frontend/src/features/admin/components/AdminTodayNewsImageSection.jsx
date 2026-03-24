import "@/features/admin/css/AdminTodayNewsImageSection.css";
import toast from "react-hot-toast";
import api from "@/api";
import handleApiError from "@/shared/utils/handleApiError";
import { useState } from "react";
import { useParams } from "react-router-dom";

export default function AdminTodayNewsImageSection() {
  const { id } = useParams(); // 오늘의 뉴스 id

  const [prompt, setPrompt] = useState("");
  const [generatedImageUrl, setGeneratedImageUrl] = useState("");
  const [generatedImageBase64, setGeneratedImageBase64] = useState("");
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);

  async function handleAiGenerate() {
    if (!prompt.trim()) {
      toast.error("이미지 생성 프롬프트를 입력해주세요.");
      return;
    }

    try {
      setLoading(true);
      const res = await api.post("/api/admin/todayNews/generateImage", {
        prompt,
      });

      const imageBase64 = res.data.imageBase64;

      setGeneratedImageBase64(imageBase64);
      setGeneratedImageUrl(`data:image/png;base64,${imageBase64}`);

      toast.success("이미지가 생성되었습니다.");
    } catch (e) {
      handleApiError(e, "생성 실패");
    } finally {
      setLoading(false);
    }
  }

  async function handleSaveImage() {
    if (!generatedImageBase64) {
      toast.error("저장할 이미지가 없습니다.");
      return;
    }

    try {
      setSaving(true);

       // 1. base64 -> Blob
      const blob = base64ToBlob(generatedImageBase64, "image/png");

      // 2. presign 요청
      const presignRes = await api.post("/api/admin/images/presign", {
        contentType: "image/png",
      });

      const { url, key } = presignRes.data;
      console.log(url, key)

       // 3. S3 직접 업로드
      await fetch(url, {
        method: "PUT",
        headers: {
          "Content-Type": "image/png",
        },
        body: blob,
      });

      // 4. DB에 imageKey 저장
      await api.put(`/api/admin/todayNews/${id}/image`, {
        imageKey: key,
      });

      toast.success("대표 이미지가 저장되었습니다.");
    } catch (e) {
      handleApiError(e, "저장 실패");
    } finally {
      setSaving(false);
    }
  }

  function handleReset() {
    setPrompt("");
    setGeneratedImageUrl("");
    setGeneratedImageBase64("");
  }

  function base64ToBlob(base64, mimeType = "image/png") {
  const byteCharacters = atob(base64);
  const byteNumbers = new Array(byteCharacters.length);

  for (let i = 0; i < byteCharacters.length; i++) {
    byteNumbers[i] = byteCharacters.charCodeAt(i);
  }

  const byteArray = new Uint8Array(byteNumbers);
  return new Blob([byteArray], { type: mimeType });
}

  return (
    <section className="admin-news-image-section">
      <div className="admin-news-image-header">
        <div>
          <h2>AI 이미지 생성</h2>
          <p>기사 요약 내용을 바탕으로 뉴스 대표 이미지를 생성할 수 있습니다.</p>
        </div>

        <button type="button" className="outline-btn">
          프롬프트 자동 작성
        </button>
      </div>

      <div className="admin-news-image-body">
        <div className="admin-news-image-form">
          <label className="admin-news-image-label">이미지 프롬프트</label>
          <textarea
            className="admin-news-image-textarea"
            placeholder="예: 국제 유가 상승과 관련된 경제 뉴스 분위기의 사실적인 이미지"
            rows={5}
            value={prompt}
            onChange={(e) => setPrompt(e.target.value)}
          />

          <div className="admin-news-image-options">
            <div className="admin-news-image-field">
              <label>스타일</label>
              <select className="admin-news-image-select" defaultValue="realistic" disabled>
                <option value="realistic">사실적</option>
                <option value="illustration">일러스트</option>
                <option value="editorial">뉴스 썸네일형</option>
              </select>
            </div>

            <div className="admin-news-image-field">
              <label>비율</label>
              <select className="admin-news-image-select" defaultValue="16:9" disabled>
                <option value="16:9">16:9</option>
                <option value="4:3">4:3</option>
                <option value="1:1">1:1</option>
              </select>
            </div>
          </div>

          <div className="admin-news-image-actions">
            <button
              onClick={handleAiGenerate}
              type="button"
              disabled={loading}
              className="primary-btn"
            >
              {loading ? "이미지 생성중..." : "이미지 생성하기"}
            </button>

            <button type="button" className="outline-btn" onClick={handleReset}>
              초기화
            </button>
          </div>
        </div>

        <div className="admin-news-image-preview-card">
          <div className="admin-news-image-preview-top">
            <span className="preview-badge">미리보기</span>
          </div>

          <div className="admin-news-image-preview-box">
            {generatedImageUrl ? (
              <img src={generatedImageUrl} alt="생성된 이미지" className="preview-image" />
            ) : (
              <div className="admin-news-image-placeholder">
                <div className="admin-news-image-placeholder-icon">🖼️</div>
                <p>생성된 이미지가 여기에 표시됩니다.</p>
                <span>아직 생성된 이미지가 없습니다.</span>
              </div>
            )}
          </div>

          <div className="admin-news-image-preview-actions">
            <button
              type="button"
              className="primary-btn"
              onClick={handleSaveImage}
              disabled={!generatedImageBase64 || saving}
            >
              {saving ? "저장중..." : "대표 이미지로 저장"}
            </button>

            <button
              type="button"
              className="danger-outline-btn"
              onClick={handleReset}
              disabled={!generatedImageBase64}
            >
              삭제
            </button>
          </div>
        </div>
      </div>
    </section>
  );
}