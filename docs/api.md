# API Overview

Base path: `/api`

## Auth

- `POST /auth/register`
- `POST /auth/login`
- `POST /auth/refresh`
- `POST /auth/forgot-password`
- `POST /auth/reset-password`
- `GET /auth/me`

The backend returns JWT access and refresh tokens. Web and extension clients send `Authorization: Bearer <accessToken>`.

## Applications

- `GET /applications`
- `POST /applications`
- `GET /applications/{id}`
- `PUT /applications/{id}`
- `DELETE /applications/{id}`

Supported filters: `month`, `year`, `company`, `role`, `skill`, `tag`, `status`, `referred`, `shortlisted`.

Statuses: `SAVED`, `APPLIED`, `OA`, `INTERVIEW`, `OFFER`, `REJECTED`, `WITHDRAWN`.

## Saved Jobs

- `GET /saved-jobs`
- `POST /saved-jobs`
- `GET /saved-jobs/{id}`
- `PUT /saved-jobs/{id}`
- `DELETE /saved-jobs/{id}`

## Analytics

- `GET /analytics`

Uses the same filters as applications and returns counts plus conversion rates.

## Review Suggestions

- `GET /review-suggestions`
- `POST /review-suggestions`
- `POST /review-suggestions/{id}/approve`
- `POST /review-suggestions/{id}/reject`

Approving can apply proposed status, referral metadata, and labels to the target application.

## Connected Emails and Gmail

- `GET /connected-emails`
- `POST /connected-emails`
- `PATCH /connected-emails/{id}/active?active=true|false`
- `POST /gmail/sync`

`/gmail/sync` is a Milestone 1 scaffold that marks sync time. Real Gmail readonly scanning is the next integration step.
