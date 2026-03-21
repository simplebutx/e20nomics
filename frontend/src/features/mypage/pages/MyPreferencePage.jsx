import { useState } from "react";
import api from "@/api";
import "@/features/mypage/css/MyPreferencePage.css";
import toast from "react-hot-toast";

const summaryDifficultyOptions = [
  { label: "경린이", value: "EASY" },
  { label: "보통", value: "NORMAL" },
  { label: "경제고수", value: "HARD" },
];

const summaryLengthOptions = [
  { label: "짧게", value: "SHORT" },
  { label: "보통", value: "MEDIUM" },
  { label: "길게", value: "LONG" },
];

const summaryFormatOptions = [
  { label: "문단형", value: "PARAGRAPH" },
  { label: "리스트형", value: "LIST" },
];

const summaryExplainStyleOptions = [
  { label: "핵심만", value: "CORE_ONLY" },
  { label: "배경까지", value: "WITH_BACKGROUND" },
  { label: "원인과 결과 중심으로", value: "CAUSE_AND_EFFECT" },
];

const termDifficultyOptions = [
  { label: "쉽게", value: "EASY" },
  { label: "보통", value: "NORMAL" },
  { label: "어렵게", value: "HARD" },
];

const termLengthOptions = [
  { label: "짧게", value: "SHORT" },
  { label: "보통", value: "MEDIUM" },
  { label: "길게", value: "LONG" },
];

export default function MyPreferencePage() {
  const [form, setForm] = useState({
    summaryDifficulty: "NORMAL",
    summaryLength: "MEDIUM",
    summaryFormat: "PARAGRAPH",
    summaryExplainStyle: "CORE_ONLY",
    termLength: "MEDIUM",
    termDifficulty: "NORMAL",
    includeRelatedConcept: false,
    includeExample: false,
  });

  function handleChange(name, value) {
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  async function handleSubmit(e) {
    e.preventDefault();

    try {
      await api.put("/api/me/preferences", form);
      toast.success("나의 선호도 설정이 업데이트 되었습니다.");
    } catch (err) {
      toast.error(err?.response?.data?.message || "수정에 실패했습니다.");
    }
  }

  return (
    <div className="preference-page">
      <form className="preference-form" onSubmit={handleSubmit}>
        <h1 className="preference-title">내 선호도 설정</h1>

        <section className="preference-section">
          <h2 className="section-title">기사 스타일</h2>

          <div className="option-group">
            <p className="option-label">내 경제 지식 수준</p>
            <div className="option-buttons">
              {summaryDifficultyOptions.map((option) => (
                <label
                  key={option.value}
                  className={`btn ${
                    form.summaryDifficulty === option.value
                      ? "btn-primary active"
                      : "btn-secondary"
                  }`}
                >
                  <input
                    type="radio"
                    name="summaryDifficulty"
                    value={option.value}
                    checked={form.summaryDifficulty === option.value}
                    onChange={(e) =>
                      handleChange("summaryDifficulty", e.target.value)
                    }
                  />
                  {option.label}
                </label>
              ))}
            </div>
          </div>

          <div className="option-group">
            <p className="option-label">답변 길이</p>
            <div className="option-buttons">
              {summaryLengthOptions.map((option) => (
                <label
                  key={option.value}
                  className={`btn ${
                    form.summaryLength === option.value
                      ? "btn-primary active"
                      : "btn-secondary"
                  }`}
                >
                  <input
                    type="radio"
                    name="summaryLength"
                    value={option.value}
                    checked={form.summaryLength === option.value}
                    onChange={(e) =>
                      handleChange("summaryLength", e.target.value)
                    }
                  />
                  {option.label}
                </label>
              ))}
            </div>
          </div>

          <div className="option-group">
            <p className="option-label">답변 형식</p>
            <div className="option-buttons">
              {summaryFormatOptions.map((option) => (
                <label
                  key={option.value}
                  className={`btn ${
                    form.summaryFormat === option.value
                      ? "btn-primary active"
                      : "btn-secondary"
                  }`}
                >
                  <input
                    type="radio"
                    name="summaryFormat"
                    value={option.value}
                    checked={form.summaryFormat === option.value}
                    onChange={(e) =>
                      handleChange("summaryFormat", e.target.value)
                    }
                  />
                  {option.label}
                </label>
              ))}
            </div>
          </div>

          <div className="option-group">
            <p className="option-label">설명 방식</p>
            <div className="option-buttons">
              {summaryExplainStyleOptions.map((option) => (
                <label
                  key={option.value}
                  className={`btn ${
                    form.summaryExplainStyle === option.value
                      ? "btn-primary active"
                      : "btn-secondary"
                  }`}
                >
                  <input
                    type="radio"
                    name="summaryExplainStyle"
                    value={option.value}
                    checked={form.summaryExplainStyle === option.value}
                    onChange={(e) =>
                      handleChange("summaryExplainStyle", e.target.value)
                    }
                  />
                  {option.label}
                </label>
              ))}
            </div>
          </div>
        </section>

        <section className="preference-section">
          <h2 className="section-title">단어 설명 스타일</h2>

          <div className="option-group">
            <p className="option-label">난이도</p>
            <div className="option-buttons">
              {termDifficultyOptions.map((option) => (
                <label
                  key={option.value}
                  className={`btn ${
                    form.termDifficulty === option.value
                      ? "btn-primary active"
                      : "btn-secondary"
                  }`}
                >
                  <input
                    type="radio"
                    name="termDifficulty"
                    value={option.value}
                    checked={form.termDifficulty === option.value}
                    onChange={(e) =>
                      handleChange("termDifficulty", e.target.value)
                    }
                  />
                  {option.label}
                </label>
              ))}
            </div>
          </div>

          <div className="option-group">
            <p className="option-label">답변 길이</p>
            <div className="option-buttons">
              {termLengthOptions.map((option) => (
                <label
                  key={option.value}
                  className={`btn ${
                    form.termLength === option.value
                      ? "btn-primary active"
                      : "btn-secondary"
                  }`}
                >
                  <input
                    type="radio"
                    name="termLength"
                    value={option.value}
                    checked={form.termLength === option.value}
                    onChange={(e) =>
                      handleChange("termLength", e.target.value)
                    }
                  />
                  {option.label}
                </label>
              ))}
            </div>
          </div>

          <div className="option-group">
            <p className="option-label">추가 옵션</p>
            <div className="option-buttons">
              <label
                className={`btn ${
                  form.includeRelatedConcept
                    ? "btn-primary active"
                    : "btn-secondary"
                }`}
              >
                <input
                  type="checkbox"
                  checked={form.includeRelatedConcept}
                  onChange={(e) =>
                    handleChange("includeRelatedConcept", e.target.checked)
                  }
                />
                유의어/반댓말도 알려주세요
              </label>

              <label
                className={`btn ${
                  form.includeExample ? "btn-primary active" : "btn-secondary"
                }`}
              >
                <input
                  type="checkbox"
                  checked={form.includeExample}
                  onChange={(e) =>
                    handleChange("includeExample", e.target.checked)
                  }
                />
                문장 예시도 들어주세요
              </label>
            </div>
          </div>
        </section>

        <div className="submit-row">
          <button type="submit" className="btn btn-primary">
            저장
          </button>
        </div>
      </form>
    </div>
  );
}