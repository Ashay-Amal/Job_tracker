import { useEffect, useState } from "react";
import { api } from "../api";
import { ApplicationForm } from "../components/ApplicationForm";
import type { JobApplication } from "../types";

export function ApplicationDetailPage({ id }: { id: string }): JSX.Element {
  const [application, setApplication] = useState<JobApplication | null>(null);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    let cancelled = false;
    api
      .application(id)
      .then((data) => {
        if (!cancelled) {
          setApplication(data);
        }
      })
      .catch((err) => {
        if (!cancelled) {
          setError(err instanceof Error ? err.message : "Application not found");
        }
      });
    return () => {
      cancelled = true;
    };
  }, [id]);

  if (error) {
    return <p className="error-text">{error}</p>;
  }

  if (!application) {
    return <div className="notice">Loading application...</div>;
  }

  return (
    <div className="page-stack narrow">
      <header className="page-header">
        <div>
          <p className="eyebrow">Editable tracking record</p>
          <h1>{application.role}</h1>
          <p>{application.company}</p>
        </div>
        <a className="secondary-button" href="/dashboard">
          Back
        </a>
      </header>
      {message && <p className="success-text">{message}</p>}
      <section className="tool-panel">
        <ApplicationForm
          initial={application}
          submitLabel="Update application"
          onSubmit={async (payload) => {
            const updated = await api.updateApplication(application.id, payload);
            setApplication(updated);
            setMessage("Application updated.");
          }}
        />
      </section>
    </div>
  );
}
