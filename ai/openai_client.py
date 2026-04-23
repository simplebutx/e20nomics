import os

import httpx
from fastapi import HTTPException
from fastapi.responses import JSONResponse


OPENAI_BASE_URL = os.getenv("OPENAI_BASE_URL", "https://api.openai.com/v1")
OPENAI_API_KEY = os.getenv("OPENAI_API_KEY")
OPENAI_MODEL = os.getenv("OPENAI_MODEL", "gpt-4o-mini")
REQUEST_TIMEOUT_SECONDS = float(os.getenv("AI_REQUEST_TIMEOUT_SECONDS", "60"))


# 헤더 만드는 함수
def _openai_headers() -> dict[str, str]:
    # OpenAI API 키가 없으면 에러
    if not OPENAI_API_KEY:
        raise HTTPException(status_code=500, detail="OPENAI_API_KEY is not configured.")

    # 있으면 헤더를 만들어서 반환
    return {
        "Authorization": f"Bearer {OPENAI_API_KEY}",
        "Content-Type": "application/json",
    }


# HTTP 요청을 그대로 전달하는 함수
async def forward_to_openai(path: str, payload: dict) -> JSONResponse:
    try:
        # httpx.AsyncClient: 파이썬에서 HTTP 요청을 보내는 클라이언트 객체
        async with httpx.AsyncClient(timeout=REQUEST_TIMEOUT_SECONDS) as client:
            response = await client.post(
                f"{OPENAI_BASE_URL}{path}",
                headers=_openai_headers(),
                json=payload,
            )
    except httpx.RequestError as exc:
        raise HTTPException(status_code=502, detail=f"OpenAI request failed: {exc}") from exc

    try:
        body = response.json()
    except ValueError:
        body = {"detail": response.text}

    return JSONResponse(status_code=response.status_code, content=body)


# 응답에서 content만 추출하는 함수
async def chat_completion_content(prompt: str, model: str | None = None) -> str:
    payload = {
        "model": model or OPENAI_MODEL,
        "messages": [
            {"role": "user", "content": prompt},
        ],
    }

    try:
        async with httpx.AsyncClient(timeout=REQUEST_TIMEOUT_SECONDS) as client:
            response = await client.post(
                f"{OPENAI_BASE_URL}/chat/completions",
                headers=_openai_headers(),
                json=payload,
            )
    except httpx.RequestError as exc:
        raise HTTPException(status_code=502, detail=f"OpenAI request failed: {exc}") from exc

    try:
        response.raise_for_status()
    except httpx.HTTPStatusError as exc:
        try:
            detail = exc.response.json()
        except ValueError:
            detail = exc.response.text
        raise HTTPException(status_code=exc.response.status_code, detail=detail) from exc

    body = response.json()
    choices = body.get("choices", [])
    if not choices:
        raise HTTPException(status_code=502, detail="AI service returned an empty chat response.")

    message = choices[0].get("message", {})
    content = message.get("content", "")
    if not content:
        raise HTTPException(status_code=502, detail="AI service returned an empty message content.")

    return content


# 이미지 base64만 추출하는 함수
async def image_generation_base64(prompt: str, model: str = "gpt-image-1", size: str = "1024x1024") -> str:
    payload = {
        "model": model,
        "prompt": prompt,
        "size": size,
    }

    try:
        async with httpx.AsyncClient(timeout=REQUEST_TIMEOUT_SECONDS) as client:
            response = await client.post(
                f"{OPENAI_BASE_URL}/images/generations",
                headers=_openai_headers(),
                json=payload,
            )
    except httpx.RequestError as exc:
        raise HTTPException(status_code=502, detail=f"OpenAI request failed: {exc}") from exc

    try:
        response.raise_for_status()
    except httpx.HTTPStatusError as exc:
        try:
            detail = exc.response.json()
        except ValueError:
            detail = exc.response.text
        raise HTTPException(status_code=exc.response.status_code, detail=detail) from exc

    body = response.json()
    data = body.get("data", [])
    if not data:
        raise HTTPException(status_code=502, detail="AI service returned an empty image response.")

    image_base64 = data[0].get("b64_json", "")
    if not image_base64:
        raise HTTPException(status_code=502, detail="AI service returned an empty image content.")

    return image_base64
