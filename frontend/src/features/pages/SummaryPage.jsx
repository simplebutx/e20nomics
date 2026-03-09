import { useState } from "react";
import api from "../../api";
import toast from "react-hot-toast";

export default function SummaryPage() {

  const [text, setText] = useState("");
  const [summary, setSummary] = useState("");
  const [loading, setLoading] = useState(false);
  const [canSave, setCanSave] = useState(false);

const submit = async () => {
  if (loading) return;
  try {
    setLoading(true);
    const res = await api.post("/api/summaries/generate", {text});
    setSummary(res.data.summary);
    setCanSave(res.data.canSave);
  } catch (e) {
    toast.error("로그인해야함");
    setCanSave(false);
  } 
  finally {
    setLoading(false);
  }
};

  async function save() {
    try {
      const res = await api.post("/api/summaries", {originalText: text, summaryText: summary, isPublic: false});
      toast.success("저장 완료");
    } catch (err) {
      toast.error(err?.response?.data?.message || "저장 실패");
    }
  }


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
      {canSave && (
        <>
        <button onClick={save}>저장</button>
        <button>다시 요약</button>
        <button>뒤로가기</button>
        </>
      )}    
    </div>
  );
}
