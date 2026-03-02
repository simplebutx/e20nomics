import axios from "axios";

export default function Health() {
const check = async () => {
  const res = await axios.get("/api/health", {
    withCredentials: true
  });
  console.log(res.data);
};

return (
    <button onClick={check}>헬스체크</button>
)
}
