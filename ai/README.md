# AI Service

This folder contains a small FastAPI service that proxies AI requests to OpenAI.

## Run locally

```bash
pip install -r requirements.txt
uvicorn main:app --reload --host 0.0.0.0 --port 8000
```

## Required environment variables

```bash
OPENAI_API_KEY=your_key
```

Optional:

```bash
OPENAI_BASE_URL=https://api.openai.com/v1
AI_REQUEST_TIMEOUT_SECONDS=60
```

## Endpoints

- `GET /health`
- `POST /v1/chat/completions`
- `POST /v1/images/generations`
