import json

from pydantic import BaseModel

from openai_client import chat_completion_content, image_generation_base64


class TodayNewsGenerateRequest(BaseModel):
    text: str
    model: str | None = None


class TodayNewsImageRequest(BaseModel):
    text: str
    model: str | None = None


def _economic_news_prompt(text: str) -> str:
    return f"""
Determine whether the following text is economic news.
Reply with YES or NO only.
If it is ambiguous, reply NO.

Text:
{text}
""".strip()


def _todaynews_summary_prompt(text: str) -> str:
    return f"""
You are writing a Korean admin-ready economic news summary.
Use only the given text and do not invent facts.

Rules:
- Write a short natural Korean title.
- Write a Korean summary in about 3 sentences.
- Return JSON only in this format:
{{
  "title": "...",
  "summary": "..."
}}

Text:
{text}
""".strip()


def _todaynews_image_prompt(text: str) -> str:
    return f"""
Create a clean, realistic, editorial-style image for economic news.
Do not include any text, letters, logos, or watermarks.
Use the following content as the visual theme:
{text}
""".strip()


def _parse_summary_content(content: str) -> dict[str, str]:
    normalized = content.strip().replace("```json", "").replace("```", "").strip()
    payload = json.loads(normalized)
    return {
        "summaryTitle": payload.get("title", ""),
        "summaryText": payload.get("summary", ""),
    }


async def generate_todaynews_summary(request: TodayNewsGenerateRequest) -> dict[str, str | bool]:
    text = request.text.strip()
    if not text:
        return {"summaryTitle": "", "summaryText": "요약할 텍스트가 비어있습니다.", "canSave": False}

    economic_result = await chat_completion_content(_economic_news_prompt(text), request.model)
    if economic_result.strip().upper() != "YES":
        return {"summaryTitle": "", "summaryText": "경제 뉴스가 아니므로 요약할 수 없습니다.", "canSave": False}

    try:
        content = await chat_completion_content(_todaynews_summary_prompt(text), request.model)
        parsed = _parse_summary_content(content)
        parsed["canSave"] = True
        return parsed
    except Exception:
        return {"summaryTitle": "", "summaryText": "제목/요약에 실패했습니다.", "canSave": False}


async def generate_todaynews_image(request: TodayNewsImageRequest) -> dict[str, str]:
    text = request.text.strip()
    if not text:
        return {"imageBase64": ""}

    image_base64 = await image_generation_base64(_todaynews_image_prompt(text))
    return {"imageBase64": image_base64}
