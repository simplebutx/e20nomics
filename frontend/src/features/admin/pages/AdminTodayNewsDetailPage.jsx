import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "@/api";
import toast from "react-hot-toast";
import "@/features/admin/css/AdminTodayNewsDetailPage.css";
import AdminTodayNewsDetailEditSection from "@/features/admin/components/AdminTodayNewsDetailEditSection";
import AdminTodayNewsDetailTermSection from "@/features/admin/components/AdminTodayNewsDetailTermSection";

export default function AdminTodayNewsDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [summaryTitle, setSummaryTitle] = useState("");
  const [summaryText, setSummaryText] = useState("");
  const [isPublished, setIsPublished] = useState(false);
  const [createdAt, setCreatedAt] = useState("");

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [deleting, setDeleting] = useState(false);

  const [allTerms, setAllTerms] = useState([]);
  const [linkedTerms, setLinkedTerms] = useState([]);
  const [selectedTermId, setSelectedTermId] = useState("");
  const [termLoading, setTermLoading] = useState(false);
  const [linking, setLinking] = useState(false);
  const [unlinkingId, setUnlinkingId] = useState(null);

  async function fetchTodayNewsDetail() {
    try {
      setLoading(true);
      const res = await api.get(`/api/admin/todayNews/${id}`);

      setSummaryTitle(res.data.summaryTitle || "");
      setSummaryText(res.data.summaryText || "");
      setIsPublished(!!res.data.isPublished);
      setCreatedAt(res.data.createdAt || "");
    } catch (err) {
      toast.error(err?.response?.data?.message || "상세 조회에 실패했습니다.");
    } finally {
      setLoading(false);
    }
  }

  async function fetchAllTerms() {
    try {
      const res = await api.get("/api/admin/terms");
      setAllTerms(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      toast.error(err?.response?.data?.message || "관리자 단어 목록 조회에 실패했습니다.");
      setAllTerms([]);
    }
  }

  async function fetchLinkedTerms() {
    try {
      setTermLoading(true);
      const res = await api.get(`/api/admin/todayNews/${id}/terms`);
      setLinkedTerms(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      toast.error(err?.response?.data?.message || "연결된 단어 조회에 실패했습니다.");
      setLinkedTerms([]);
    } finally {
      setTermLoading(false);
    }
  }

  async function fetchPageData() {
    await Promise.all([
      fetchTodayNewsDetail(),
      fetchAllTerms(),
      fetchLinkedTerms(),
    ]);
  }

  useEffect(() => {
    if (!id) return;
    fetchPageData();
  }, [id]);

  async function handleUpdate() {
    if (saving) return;

    if (!summaryTitle.trim() || !summaryText.trim()) {
      toast.error("제목과 요약문을 입력해 주세요.");
      return;
    }

    try {
      setSaving(true);

      await api.put(`/api/admin/todayNews/${id}`, {
        summaryTitle: summaryTitle.trim(),
        summaryText: summaryText.trim(),
        isPublished,
      });

      toast.success("수정이 완료되었습니다.");
      await fetchTodayNewsDetail();
    } catch (err) {
      toast.error(err?.response?.data?.message || "수정에 실패했습니다.");
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete() {
    if (deleting) return;

    const ok = window.confirm("정말 삭제하시겠습니까?");
    if (!ok) return;

    try {
      setDeleting(true);
      await api.delete(`/api/admin/todayNews/${id}`);
      toast.success("삭제되었습니다.");
      navigate("/admin");
    } catch (err) {
      toast.error(err?.response?.data?.message || "삭제에 실패했습니다.");
    } finally {
      setDeleting(false);
    }
  }

  async function handleTogglePublished() {
    const nextValue = !isPublished;

    try {
      await api.put(`/api/admin/todayNews/${id}`, {
        summaryTitle: summaryTitle.trim(),
        summaryText: summaryText.trim(),
        isPublished: nextValue,
      });

      setIsPublished(nextValue);
      toast.success(nextValue ? "공개 게시되었습니다." : "게시가 취소되었습니다.");
    } catch (err) {
      toast.error(err?.response?.data?.message || "상태 변경에 실패했습니다.");
    }
  }

  async function handleLinkTerm() {
    if (!selectedTermId) {
      toast.error("연결할 단어를 선택해 주세요.");
      return;
    }

    if (linking) return;

    try {
      setLinking(true);

      await api.post(`/api/admin/todayNews/${id}/terms`, {
        termId: Number(selectedTermId),
      });

      toast.success("단어가 연결되었습니다.");
      setSelectedTermId("");
      await fetchLinkedTerms();
    } catch (err) {
      toast.error(err?.response?.data?.message || "단어 연결에 실패했습니다.");
    } finally {
      setLinking(false);
    }
  }

  async function handleUnlinkTerm(termId) {
    if (unlinkingId) return;

    try {
      setUnlinkingId(termId);

      await api.delete(`/api/admin/todayNews/${id}/terms/${termId}`);

      toast.success("단어 연결이 해제되었습니다.");
      await fetchLinkedTerms();
    } catch (err) {
      toast.error(err?.response?.data?.message || "단어 연결 해제에 실패했습니다.");
    } finally {
      setUnlinkingId(null);
    }
  }

  const availableTerms = useMemo(() => {
    const linkedIds = new Set(linkedTerms.map((term) => term.id));
    return allTerms.filter((term) => !linkedIds.has(term.id));
  }, [allTerms, linkedTerms]);

  function formatDate(dateString) {
    if (!dateString) return "";
    return new Date(dateString).toLocaleString("ko-KR");
  }

  if (loading) {
    return (
      <div className="admin-today-news-detail-page">
        <div className="admin-today-news-detail-card">
          <p className="admin-today-news-detail-loading">불러오는 중...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="admin-today-news-detail-page">
      <AdminTodayNewsDetailEditSection
        summaryTitle={summaryTitle}
        summaryText={summaryText}
        isPublished={isPublished}
        createdAt={createdAt}
        saving={saving}
        deleting={deleting}
        setSummaryTitle={setSummaryTitle}
        setSummaryText={setSummaryText}
        onUpdate={handleUpdate}
        onDelete={handleDelete}
        onTogglePublished={handleTogglePublished}
        onGoList={() => navigate("/admin/todayNews")}
        formatDate={formatDate}
      />

      <AdminTodayNewsDetailTermSection
        linkedTerms={linkedTerms}
        availableTerms={availableTerms}
        selectedTermId={selectedTermId}
        setSelectedTermId={setSelectedTermId}
        termLoading={termLoading}
        linking={linking}
        unlinkingId={unlinkingId}
        onLinkTerm={handleLinkTerm}
        onUnlinkTerm={handleUnlinkTerm}
      />
    </div>
  );
}