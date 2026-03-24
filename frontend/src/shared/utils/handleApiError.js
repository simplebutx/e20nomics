import toast from "react-hot-toast";

export default function handleApiError(error, defaultMessage = "요청 처리에 실패했습니다.") {
    const status = error?.response?.status;

    if (status === 401) return; // 인터셉터 전담

    if (status === 404) {
    toast.error("존재하지 않는 데이터입니다.");
    return;
    }

    if (status === 403) {
    toast.error("접근 권한이 없습니다.");
    return;
    }

    toast.error(error?.response?.data?.message || defaultMessage);
}