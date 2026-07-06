# Railway Free-Test Deployment

Recommended split:

- Frontend: Vercel project from `frontend/`
- Backend: Railway service from `backend/`
- Database: Railway PostgreSQL
- Redis: Railway Redis when sync locks/rate limits are enabled

Required backend variables:

```txt
SPRING_DATASOURCE_URL=jdbc:postgresql://...
SPRING_DATASOURCE_USERNAME=...
SPRING_DATASOURCE_PASSWORD=...
APP_JWT_SECRET=...
APP_FRONTEND_BASE_URL=https://your-vercel-app.vercel.app
GOOGLE_CLIENT_ID=...
GOOGLE_CLIENT_SECRET=...
OLLAMA_BASE_URL=
```

AI behavior:

- Leave `OLLAMA_BASE_URL` empty for a normal free deployment.
- The API exposes deterministic label extraction and marks AI as unavailable until an Ollama endpoint is configured.
