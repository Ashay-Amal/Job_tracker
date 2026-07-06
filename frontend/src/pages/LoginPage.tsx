import { useState } from "react";
import { Chrome, Mail } from "lucide-react";
import { useAuth } from "../state/AuthContext";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080";

export function LoginPage(): JSX.Element {
  const { login } = useAuth();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  async function submit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError("");
    try {
      await login(email, password);
      window.location.href = "/dashboard";
    } catch (err) {
      setError(err instanceof Error ? err.message : "Login failed");
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
        <h1>Login</h1>
        <form className="auth-form" onSubmit={submit}>
          <label>
            Email
            <input type="email" required value={email} onChange={(event) => setEmail(event.target.value)} />
          </label>
          <label>
            Password
            <input type="password" required value={password} onChange={(event) => setPassword(event.target.value)} />
          </label>
          {error && <p className="error-text">{error}</p>}
          <button className="primary-button full-width" type="submit">
            <Mail size={16} /> Login
          </button>
          <a className="secondary-button full-width" href={`${apiBaseUrl}/oauth2/authorization/google`}>
            <Chrome size={16} /> Login with Google
          </a>
        </form>
        <div className="auth-links">
          <a href="/register">Create account</a>
          <a href="/forgot-password">Forgot password</a>
        </div>
      </section>
    </main>
  );
}
