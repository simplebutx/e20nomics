import { useEffect, useState } from "react";
import api from "../../api"
import toast from "react-hot-toast";

export default function TodayNewsPage() {

    const [announcements, setAnnouncements] = useState([]);

    async function fetchAnnouncements() {
        try { const res = await api.get("/api/announcements");
        const data = res.data;
        setAnnouncements(Array.isArray(data) ? data : []);
        } catch(err) {
            toast.error(err?.response?.data?.message || "불러오기 실패");
        }
    }

    useEffect(()=> {
        fetchAnnouncements();
    }, [])


    return (
        <>
            <h1>오늘의 뉴스</h1>
            {announcements.map((a) => (
                <div key={a.id}>
                    <p>{a.summaryText}</p>
                </div>
            ))}
        </>
    )
}