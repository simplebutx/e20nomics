from fastapi import FastAPI
from fastapi.responses import JSONResponse

from openai_client import forward_to_openai
from summary_service import SummaryGenerateRequest, generate_summary
from term_service import (
    AdminTermGenerateRequest,
    MyTermGenerateRequest,
    generate_admin_term,
    generate_my_term,
)
from todaynews_service import (
    TodayNewsGenerateRequest,
    TodayNewsImageRequest,
    generate_todaynews_image,
    generate_todaynews_summary,
)


app = FastAPI(title="e20nomics-ai-service")


@app.get("/health")
async def health() -> dict[str, str]:
    return {"status": "ok"}


# summary 생성 요청
@app.post("/internal/summary/generate")
async def summary_generate(request: SummaryGenerateRequest) -> dict[str, str | bool]:  #dict[str, str | bool]: 키/값 반환타입 
    return await generate_summary(request)


# my term 생성 요청
# spring에서 보낸 reqBody에 담긴 값을디 request로 들어옴
@app.post("/internal/terms/my/generate")
async def my_term_generate(request: MyTermGenerateRequest) -> dict[str, str | bool]:
    return await generate_my_term(request)


# admin term 생성 요청
@app.post("/internal/terms/admin/generate")
async def admin_term_generate(request: AdminTermGenerateRequest) -> dict[str, str | bool]:
    return await generate_admin_term(request)


# today news summary 생성 요청
@app.post("/internal/todaynews/generate")
async def todaynews_generate(request: TodayNewsGenerateRequest) -> dict[str, str | bool]:
    return await generate_todaynews_summary(request)


# today news image 생성 요청
@app.post("/internal/todaynews/generate-image")
async def todaynews_generate_image(request: TodayNewsImageRequest) -> dict[str, str]:
    return await generate_todaynews_image(request)


# 채팅 completions 프록시 요청
@app.post("/v1/chat/completions")
async def chat_completions(payload: dict) -> JSONResponse:
    return await forward_to_openai("/chat/completions", payload)


# 이미지 생성 프록시 요청
@app.post("/v1/images/generations")
async def image_generations(payload: dict) -> JSONResponse:
    return await forward_to_openai("/images/generations", payload)
