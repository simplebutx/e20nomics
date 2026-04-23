from pydantic import BaseModel

from openai_client import chat_completion_content
from term_preprocessor import (
    classify_term_with_keywords,
    preprocess_term_text,
    judge_economic_term,
)


class MyTermGenerateRequest(BaseModel):
    text: str
    term_length: str
    term_difficulty: str
    include_example: bool
    include_related_concept: bool
    model: str | None = None


class AdminTermGenerateRequest(BaseModel):
    text: str
    model: str | None = None


# 경제 용어 요약 요청
def _my_term_prompt(
    term: str,
    term_length: str,
    term_difficulty: str,
    include_example: bool,
    include_related_concept: bool,
) -> str:
    example_prompt = "Include a short example." if include_example else "Do not include an example."
    related_prompt = "Include a related or commonly confused concept." if include_related_concept else "Do not include related concepts."

    return f"""
You explain economics terms to Korean learners.
Explain the term in natural Korean.

User preferences:
- Length: {term_length}
- Difficulty: {term_difficulty}
- Example: {example_prompt}
- Related concept: {related_prompt}

Rules:
- Do not use bullets, numbering, or headings.
- Do not invent unsupported facts.
- Output only the definition text in Korean.

Term:
{term}
""".strip()


def _admin_term_prompt(term: str) -> str:
    return f"""
You explain economics terms clearly for beginners.
Explain the term in natural Korean in 2-3 sentences.
Do not use bullets, numbering, or headings.
Output only the definition text.

Term:
{term}
""".strip()


# 문자 정리 함수
def _normalize_definition(content: str) -> str:
    return content.strip().replace("\n", " ").strip('"')


# 실제 로직
async def generate_my_term(request: MyTermGenerateRequest) -> dict[str, str | bool]:
    # 1차: 입력 전처리
    term = preprocess_term_text(request.text)
    if not term:
        return {"term": "", "definition": "용어를 입력해주세요.", "canSave": False}

    try:
        # 2차: 키워드/간단 분류기
        keyword_result = classify_term_with_keywords(term)
        if keyword_result.is_clearly_non_economic:
            return {"term": term, "definition": "경제와 관련한 용어가 아닙니다.", "canSave": False}

        # 3차: 경제/비경제 용어 내부 최종 판단
        if not judge_economic_term(term, keyword_result):
            return {"term": term, "definition": "경제와 관련한 용어가 아닙니다.", "canSave": False}

        # 응답 추출 함수 사용 (용어 요약)
        content = await chat_completion_content(
            _my_term_prompt(
                term=term,
                term_length=request.term_length,
                term_difficulty=request.term_difficulty,
                include_example=request.include_example,
                include_related_concept=request.include_related_concept,
            ),
            request.model,
        )

        # 응답 문자열 정리
        definition = _normalize_definition(content)
        if not definition:
            return {"term": term, "definition": "정의 생성에 실패했습니다.", "canSave": False}

        # spring에 보낼 응답 return
        return {"term": term, "definition": definition, "canSave": True}
    except Exception:
        return {"term": term, "definition": "정의 생성 중 오류가 발생했습니다.", "canSave": False}


async def generate_admin_term(request: AdminTermGenerateRequest) -> dict[str, str | bool]:
    term = preprocess_term_text(request.text)
    if not term:
        return {"term": "", "definition": "용어를 입력해주세요.", "canSave": False}

    try:
        keyword_result = classify_term_with_keywords(term)
        if keyword_result.is_clearly_non_economic:
            return {"term": term, "definition": "경제와 관련한 용어가 아닙니다.", "canSave": False}

        if not judge_economic_term(term, keyword_result):
            return {"term": term, "definition": "경제와 관련한 용어가 아닙니다.", "canSave": False}

        content = await chat_completion_content(_admin_term_prompt(term), request.model)
        definition = _normalize_definition(content)
        if not definition:
            return {"term": term, "definition": "정의 생성에 실패했습니다.", "canSave": False}

        return {"term": term, "definition": definition, "canSave": True}
    except Exception:
        return {"term": term, "definition": "정의 생성 중 오류가 발생했습니다.", "canSave": False}
