# Milestones

## Milestone 1: Usable Tracker

- Scaffold monorepo and local Docker infrastructure.
- Implement auth, JWT, password reset, and Google login scaffolding.
- Implement manual applications, saved jobs, dashboard filters, analytics, editable application detail page.
- Implement connected email settings and manual sync endpoint scaffold.
- Implement review queue and confidence bands.
- Implement unpacked Chrome extension with manual capture.

## Milestone 2: Gmail Integration

- Add Google OAuth flow for connecting multiple Gmail accounts.
- Store encrypted Gmail refresh tokens.
- Implement readonly Gmail search for last 90 days.
- Store message metadata, snippets, and extracted facts only.
- Generate review suggestions for status updates and referral detection.

## Milestone 3: AI Automation

- Call Ollama/Qwen through `OLLAMA_BASE_URL`.
- Classify job-related emails.
- Extract status/referral/label suggestions with evidence.
- Add unmatched update queue when multiple applications are possible.

## Milestone 4: Beta Hardening

- Add Redis-backed sync locks and rate limits.
- Add audit log for review approvals.
- Add stronger error handling and onboarding.
- Prepare Railway/Vercel beta deployment.

## Milestone 5: Production

- Add Flyway migrations.
- Add token encryption and secret rotation policy.
- Add AWS ECS Fargate deployment through GitHub Actions.
- Add observability, backups, and production security checks.
