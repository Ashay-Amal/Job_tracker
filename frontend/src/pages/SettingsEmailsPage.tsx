import { useEffect, useState } from "react";
import { RefreshCw } from "lucide-react";
import { api } from "../api";
import type { ConnectedEmail } from "../types";
import { formatDate } from "../utils";

export function SettingsEmailsPage(): JSX.Element {
  const [emails, setEmails] = useState<ConnectedEmail[]>([]);
  const [emailAddress, setEmailAddress] = useState("");
  const [displayName, setDisplayName] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  async function load() {
    setEmails(await api.connectedEmails());
  }

  useEffect(() => {
    void load().catch((err) => setError(err instanceof Error ? err.message : "Unable to load connected emails"));
  }, []);

  async function submit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError("");
    setMessage("");
    try {
      await api.createConnectedEmail({ emailAddress, displayName });
      setEmailAddress("");
      setDisplayName("");
      await load();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unable to add email");
    }
  }

  async function sync(id: string) {
    setError("");
    setMessage("");
    try {
      const response = await api.syncGmail(id);
      setMessage(response.message);
      await load();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unable to sync Gmail");
    }
  }

  return (
    <div className="page-stack narrow">
      <header className="page-header">
        <div>
          <p className="eyebrow">Multi-email tracking</p>
          <h1>Connected emails</h1>
        </div>
      </header>
      <section className="tool-panel">
        <h2>Add Gmail account</h2>
        <form className="form-grid" onSubmit={submit}>
          <label>
            Email address
            <input type="email" required value={emailAddress} onChange={(event) => setEmailAddress(event.target.value)} />
          </label>
          <label>
            Display name
            <input value={displayName} onChange={(event) => setDisplayName(event.target.value)} />
          </label>
          <button className="primary-button span-2" type="submit">
            Add email
          </button>
        </form>
      </section>
      {message && <p className="success-text">{message}</p>}
      {error && <p className="error-text">{error}</p>}
      <section className="record-list">
        {emails.map((email) => (
          <article className="record-card" key={email.id}>
            <div>
              <strong>{email.emailAddress}</strong>
              <p>{email.displayName || email.provider}</p>
              <small>Last sync: {formatDate(email.lastSyncedAt)}</small>
            </div>
            <button className="secondary-button" type="button" onClick={() => void sync(email.id)}>
              <RefreshCw size={16} /> Manual sync
            </button>
          </article>
        ))}
      </section>
    </div>
  );
}
