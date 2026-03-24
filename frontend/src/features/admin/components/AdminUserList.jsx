import { useEffect, useState } from "react";
import api from "@/api";
import toast from "react-hot-toast";
import handleApiError from "@/shared/utils/handleApiError";
import "@/features/admin/css/AdminUserList.css";

export default function AdminUserList() {
  const [users, setUsers] = useState([]);

  async function fetchUserList() {
    try {
      const res = await api.get("/api/admin/users");
      const data = res.data;
      setUsers(Array.isArray(data) ? data : []);
    } catch (e) {
      handleApiError(e, "조회 실패")
    }
  }

  useEffect(() => {
    fetchUserList();
  }, []);

  if (users.length === 0) {
    return <p className="admin-list-empty">회원이 없습니다.</p>;
  }

  return (
    <div className="admin-user-table-wrap">
      <table className="admin-user-table">
        <thead>
          <tr>
            <th>이메일</th>
            <th>이름</th>
            <th>닉네임</th>
          </tr>
        </thead>
        <tbody>
          {users.map((u) => (
            <tr key={u.id}>
              <td>{u.email}</td>
              <td>{u.userName}</td>
              <td>{u.displayName}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}