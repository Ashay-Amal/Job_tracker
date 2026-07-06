# Architecture

## Product Model

Job Tracker is centered on one user account with many connected Gmail accounts. Applications are not tied to a single inbox because the application email, LinkedIn email, resume email, and recruiter reply email may differ.

## Core Data

- User account: local password login and Google login.
- Connected email: Gmail account metadata and future readonly OAuth token.
- Job application: company, role, link, description, one primary status, shortlist/referral flags, referral evidence, labels, tags, notes.
- Saved job: future application candidate captured manually from web or extension.
- Review suggestion: AI/Gmail proposed update with confidence, evidence, proposed status/referral/labels, and approval state.

## Reliability Boundary

The LLM is a suggestion engine, not the source of truth. Medium and high confidence findings become review items. Low confidence findings are ignored or left unmatched unless deterministic evidence is added later.

Confidence bands:

- High: `>= 0.80`
- Medium: `0.60-0.79`
- Low: `< 0.60`

## Gmail Boundary

Private beta sync is manual. Initial sync should scan only the last 90 days. The app stores Gmail message IDs, thread IDs, sender, subject, date, snippets, and extracted facts. It does not store full email bodies by default.

## Extension Boundary

The Manifest V3 extension authenticates independently with the Job Tracker backend. It does not depend on the active Chrome/Google account. Milestone 1 capture is manual and editable.
