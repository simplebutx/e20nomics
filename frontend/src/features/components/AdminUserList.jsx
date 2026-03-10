import { useEffect, useState } from "react";
import api from "../../api"
import toast from "react-hot-toast";

export default function AdminUserList() {

    const [users, setUsers] = useState([]);

    async function fetchUserList() {
        try {
            const res = await api.get("/api/admin/users");
            const data = res.data;
            setUsers(Array.isArray(data) ? data : []);
        } catch(err) {
            toast.error(err?.response?.data?.message || "불러오기 실패");
        }
    }

    useEffect(()=> {
        fetchUserList();
    }, []);


    return (
        <>
            {users.map((u) => (
                <div key={u.id}>
                    <p>이메일: {u.email} 이름: {u.userName} 닉네임: {u.displayName} </p>
                </div>
            ))}
        </>
    )
}