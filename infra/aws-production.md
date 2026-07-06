# AWS Production Target

Target stack:

- ECR repositories for backend and frontend images.
- ECS Fargate services behind an Application Load Balancer.
- RDS PostgreSQL for durable application data.
- ElastiCache Redis for refresh-token revocation, rate limiting, and future Gmail sync locks.
- Secrets Manager or SSM Parameter Store for database credentials, JWT secret, Google OAuth credentials, SMTP, and Ollama URL.
- Optional internal Ollama service, addressed by `OLLAMA_BASE_URL`.

Deployment flow:

1. GitHub Actions builds and tests backend/frontend.
2. Actions builds Docker images and pushes to ECR.
3. Actions updates ECS task definitions and services.
4. Database migrations should move to Flyway before production hardening.

Security notes:

- Gmail OAuth refresh tokens must be encrypted at rest before real Gmail integration is enabled.
- Full email bodies remain out of persistent storage unless the privacy policy is explicitly changed.
- AI suggestions must stay human-reviewed; no automatic status overwrites.
