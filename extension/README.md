# Job Tracker Extension

Private beta Manifest V3 extension. Load unpacked from `extension/src` during development.

The extension has its own login form and does not depend on the Google account active in Chrome. Users enter the Job Tracker API URL, sign in with their tracker account, then save or mark a job as applied with manual fields.

Milestone 1 intentionally avoids site-specific scraping. The popup can read the current tab URL and title to reduce typing, but the user remains responsible for confirming company, role, description, tags, and status.
