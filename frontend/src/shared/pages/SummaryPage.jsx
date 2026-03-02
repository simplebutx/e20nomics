import { useState } from "react";
import axios from "axios";

export default function SummaryPage() {

  const [text, setText] = useState("");
  const [summary, setSummary] = useState("");
  const [loading, setLoading] = useState(false);

const submit = async () => {
  if (loading) return;

  try {
    setLoading(true);

    const res = await axios.post("/api/briefings", {
      text
    }, {
      withCredentials: true
    });

    setSummary(res.data.summary);
  } finally {
    setLoading(false);
  }
};
  return (
    <div>
      <textarea
        rows={10}
        value={text}
        onChange={(e) => setText(e.target.value)}
        placeholder="기사 붙여넣기"
      />
      <br/>
      <button onClick={submit} disabled={loading}>   {/* 클릭 연타 방지용 버튼 비활성화 */}
       {loading ? "요약 중..." : "요약하기"}
      </button>

      <hr/>

      <p>{summary}</p>
    </div>
  );
}
