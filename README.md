# Job Tracker

Private beta job application tracker with a React web app, Spring Boot API, PostgreSQL, Gmail tracking scaffolding, Chrome extension capture, and optional Ollama-powered AI.

## Project Layout

```txt
backend/     Spring Boot API
frontend/    React + TypeScript web app
extension/   Chrome Manifest V3 extension
infra/       Docker, Railway, AWS notes
docs/        Architecture, API, privacy, milestones
```

## Local Development

1. Copy `.env.example` to `.env` and fill secrets.
2. Start infrastructure:

```bash
docker compose up postgres redis
```

3. Start backend from `backend/` with Gradle:

```bash
gradle bootRun
```

4. Start frontend from `frontend/`:

```bash
pnpm install
pnpm dev
```

5. Load the extension unpacked from `extension/src` in Chrome.

## Milestone 1

- Email/password auth, Google login scaffolding, JWT access/refresh tokens.
- Manual application tracking, saved jobs, dashboard filters, analytics.
- Editable application records with referral and label metadata.
- Review queue for AI/Gmail suggestions.
- Manual Chrome extension capture.
- Connected email records and manual Gmail sync endpoint scaffold.

Gmail reading and Ollama inference are intentionally safe by default: the backend stores metadata/extracted facts, does not persist full email bodies, and never overwrites application state without a review action.
