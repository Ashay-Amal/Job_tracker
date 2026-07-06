# Privacy Notes

Private beta defaults:

- Gmail sync is manual.
- Every tracked Gmail account must be explicitly connected.
- Initial sync scans the last 90 days.
- The app stores metadata, snippets, and extracted facts, not full email bodies.
- The LLM does not silently update application records.
- Users can edit every application field, including labels and referral status.

Before production:

- Encrypt Gmail refresh tokens at rest.
- Add audit logs for status/referral changes.
- Document data deletion and export flows.
- Add a clear privacy policy for Gmail scopes and AI processing.
