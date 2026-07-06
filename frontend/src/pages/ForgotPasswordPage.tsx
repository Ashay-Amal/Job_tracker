import { useState } from "react";
import { api } from "../api";

export function ForgotPasswordPage(): JSX.Element {
  const [email, setEmail] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  async function submit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setMessage("");
    setError("");
    try {
      const response = await api.forgotPassword(email);
      setMessage(response.message);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Reset request failed");
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
        <h1>Reset password</h1>
        <form className="auth-form" onSubmit={submit}>
          <label>
            Account email
            <input type="email" required value={email} onChange={(event) => setEmail(event.target.value)} />
          </label>
          {message && <p className="success-text">{message}</p>}
          {error && <p className="error-text">{error}</p>}
          <button className="primary-button full-width" type="submit">
            Send reset link
          </button>
        </form>
        <div className="auth-links">
          <a href="/login">Back to login</a>
        </div>
      </section>
    </main>
  );
}
