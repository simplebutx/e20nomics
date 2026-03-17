import axios from "axios";
import "@/shared/css/button.css"

export default function Health() {
const check = async () => {
  const res = await axios.get("/api/health", {
    withCredentials: true
  });
  console.log(res.data);
};

return (
  <>
    <button onClick={check}>헬스체크</button>

    <button class="btn btn-primary">AI 생성</button>
    <button class="btn btn-secondary">취소</button>
    <button class="btn btn-danger">삭제</button>
    <button class="btn btn-outline">직접 등록</button>
  </>
)
}
