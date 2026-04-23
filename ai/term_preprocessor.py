import re
from dataclasses import dataclass


ECONOMIC_KEYWORDS = {
    "금리",
    "환율",
    "주가",
    "인플레이션",
    "디플레이션",
    "실업률",
    "기준금리",
    "통화량",
    "유동성",
    "경기침체",
    "gdp",
    "cpi",
    "ppi",
    "코스피",
    "코스닥",
    "채권",
    "국채",
    "회사채",
    "주식",
    "배당",
    "공매도",
    "etf",
    "per",
    "pbr",
    "roe",
    "매출",
    "영업이익",
    "순이익",
    "시가총액",
    "환헤지",
    "환차익",
    "자산",
    "부채",
    "자본",
    "예금",
    "적금",
    "대출",
    "신용등급",
    "물가",
    "무역수지",
    "관세",
    "수출",
    "수입",
    "파생상품",
    "선물",
    "옵션",
    "리츠",
    "펀드",
    "재정정책",
    "통화정책",
    "기축통화",
    "기업가치",
    "밸류에이션",
}

NON_ECONOMIC_KEYWORDS = {
    "강아지",
    "고양이",
    "레시피",
    "축구",
    "야구",
    "게임",
    "영화",
    "드라마",
    "아이돌",
    "노래",
    "맛집",
    "여행지",
    "헬스",
    "피부관리",
    "메이크업",
    "애니메이션",
    "화장품",
    "다이어트",
    "연예인",
}

ECONOMIC_SUFFIXES = (
    "금리",
    "지수",
    "수익률",
    "물가",
    "주",
    "채",
    "세",
    "률",
    "정책",
    "시장",
    "통화",
    "재정",
    "펀드",
    "채권",
)


@dataclass
class KeywordClassificationResult:
    is_clearly_non_economic: bool
    keyword_score: int
    matched_keywords: list[str]


def preprocess_term_text(text: str) -> str:
    # 1차: 입력 전처리
    normalized = text.strip()
    normalized = normalized.replace("\u200b", " ")
    normalized = normalized.replace('"', "").replace("'", "")
    normalized = re.sub(r"\s+", " ", normalized)
    normalized = re.sub(r"[()\[\]{}]+", " ", normalized)
    normalized = re.sub(r"\s+", " ", normalized)
    return normalized.strip()


def classify_term_with_keywords(text: str) -> KeywordClassificationResult:
    # 2차: 키워드/간단 분류기
    lowered = text.lower()

    matched_economic = sorted(keyword for keyword in ECONOMIC_KEYWORDS if keyword in lowered)
    matched_non_economic = sorted(keyword for keyword in NON_ECONOMIC_KEYWORDS if keyword in lowered)

    keyword_score = len(matched_economic) - len(matched_non_economic)
    is_clearly_non_economic = len(matched_non_economic) > 0 and keyword_score < 0

    return KeywordClassificationResult(
        is_clearly_non_economic=is_clearly_non_economic,
        keyword_score=keyword_score,
        matched_keywords=matched_economic,
    )


def _suffix_score(text: str) -> int:
    compact = text.replace(" ", "")
    return sum(1 for suffix in ECONOMIC_SUFFIXES if compact.endswith(suffix))


def judge_economic_term(text: str, keyword_result: KeywordClassificationResult | None = None) -> bool:
    # 3차: 내부 경량 모델(규칙 기반)로 경제/비경제 용어 최종 판단
    if keyword_result is None:
        keyword_result = classify_term_with_keywords(text)

    if keyword_result.is_clearly_non_economic:
        return False

    compact = text.replace(" ", "")
    suffix_score = _suffix_score(text)
    token_count = len([token for token in text.split(" ") if token])
    contains_number = bool(re.search(r"\d", compact))

    score = keyword_result.keyword_score
    score += suffix_score

    if contains_number:
        score += 1

    if token_count >= 3:
        score -= 1

    if len(compact) <= 1:
        score -= 2

    return score > 0
