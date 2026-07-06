import { useState } from "react";
import { useAuth } from "../state/AuthContext";

export function RegisterPage(): JSX.Element {
  const { register } = useAuth();
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  async function submit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError("");
    try {
      await register(email, password, fullName);
      window.location.href = "/dashboard";
    } catch (err) {
      setError(err instanceof Error ? err.message : "Registration failed");
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
        <h1>Create account</h1>
        <form className="auth-form" onSubmit={submit}>
          <label>
            Full name
            <input required value={fullName} onChange={(event) => setFullName(event.target.value)} />
          </label>
          <label>
            Email
            <input type="email" required value={email} onChange={(event) => setEmail(event.target.value)} />
          </label>
          <label>
            Password
            <input
              type="password"
              required
              minLength={8}
              value={password}
              onChange={(event) => setPassword(event.target.value)}
            />
          </label>
          {error && <p className="error-text">{error}</p>}
          <button className="primary-button full-width" type="submit">
            Create account
          </button>
        </form>
        <div className="auth-links">
          <a href="/login">Already have an account?</a>
        </div>
      </section>
    </main>
  );
}
