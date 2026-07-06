import { useState } from "react";
import { api } from "../api";

export function ResetPasswordPage(): JSX.Element {
  const params = new URLSearchParams(window.location.search);
  const [token] = useState(params.get("token") ?? "");
  const [newPassword, setNewPassword] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  async function submit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setMessage("");
    setError("");
    try {
      const response = await api.resetPassword(token, newPassword);
      setMessage(response.message);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Password reset failed");
    }
  }

  return (
    <main className="auth-page">
      <section className="auth-panel">
        <a className="brand auth-brand" href="/">
          <span className="brand-mark">JT</span>
          <span>
            <strong>Job Tracker</strong>
            <small>Private beta</small>
          </span>
        </a>
        <h1>Choose new password</h1>
        <form className="auth-form" onSubmit={submit}>
          <label>
            New password
            <input
              type="password"
              required
              minLength={8}
              value={newPassword}
              onChange={(event) => setNewPassword(event.target.value)}
            />
          </label>
          {message && <p className="success-text">{message}</p>}
          {error && <p className="error-text">{error}</p>}
          <button className="primary-button full-width" type="submit" disabled={!token}>
            Reset password
          </button>
        </form>
        <div className="auth-links">
          <a href="/login">Back to login</a>
        </div>
      </section>
    </main>
  );
}
