import json

from pydantic import BaseModel

from openai_client import chat_completion_content


class SummaryGenerateRequest(BaseModel):
    text: str
    summary_length: str
    summary_difficulty: str
    summary_format: str
    summary_explain_style: str
    model: str | None = None


def _economic_news_prompt(text: str) -> str:
    return f"""
Determine whether the following text is economic news.
Reply with YES or NO only.
If it is ambiguous, reply NO.

Text:
{text}
""".strip()


def _summary_prompt(
    text: str,
    summary_length: str,
    summary_difficulty: str,
    summary_format: str,
    summary_explain_style: str,
) -> str:
    return f"""
You are an assistant that writes Korean economic news summaries.
Use only the provided text. Do not invent missing facts.

User preferences:
- Length: {summary_length}
- Difficulty: {summary_difficulty}
- Format: {summary_format}
- Explanation style: {summary_explain_style}

Write a natural Korean title and summary that follow the preferences.
Return JSON only in this format:
{{
  "title": "...",
  "summary": "..."
}}

Text:
{text}
""".strip()


def _parse_summary_content(content: str) -> dict[str, str]:
    normalized = content.strip().replace("```json", "").replace("```", "").strip()
    payload = json.loads(normalized)
    return {
        "summaryTitle": payload.get("title", ""),
        "summaryText": payload.get("summary", ""),
    }


async def generate_summary(request: SummaryGenerateRequest) -> dict[str, str | bool]:
    text = request.text.strip()
    if not text:
        return {"summaryTitle": "", "summaryText": "요약할 텍스트가 비어있습니다.", "canSave": False}

    economic_result = await chat_completion_content(_economic_news_prompt(text), request.model)
    if economic_result.strip().upper() != "YES":
        return {"summaryTitle": "", "summaryText": "경제 뉴스가 아니므로 요약할 수 없습니다.", "canSave": False}

    try:
        content = await chat_completion_content(
            _summary_prompt(
                text=text,
                summary_length=request.summary_length,
                summary_difficulty=request.summary_difficulty,
                summary_format=request.summary_format,
                summary_explain_style=request.summary_explain_style,
            ),
            request.model,
        )
        parsed = _parse_summary_content(content)
        parsed["canSave"] = True
        return parsed
    except Exception:
        return {"summaryTitle": "", "summaryText": "제목/요약에 실패했습니다.", "canSave": False}
