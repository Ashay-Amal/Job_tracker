import { useEffect, useState } from "react";
import { Plus } from "lucide-react";
import { api } from "../api";
import type { SavedJob } from "../types";
import { splitList } from "../utils";

interface SavedJobDraft {
  company: string;
  role: string;
  jobLink: string;
  description: string;
  notes: string;
  skills: string;
  tags: string;
}

const emptyDraft: SavedJobDraft = {
  company: "",
  role: "",
  jobLink: "",
  description: "",
  notes: "",
  skills: "",
  tags: ""
};

export function SavedJobsPage(): JSX.Element {
  const [savedJobs, setSavedJobs] = useState<SavedJob[]>([]);
  const [draft, setDraft] = useState<SavedJobDraft>(emptyDraft);
  const [error, setError] = useState("");

  async function load() {
    setSavedJobs(await api.savedJobs());
  }

  useEffect(() => {
    void load().catch((err) => setError(err instanceof Error ? err.message : "Unable to load saved jobs"));
  }, []);

  async function submit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError("");
    try {
      await api.createSavedJob({
        ...draft,
        skills: splitList(draft.skills),
        tags: splitList(draft.tags)
      });
      setDraft(emptyDraft);
      await load();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unable to save job");
    }
  }

  function update<K extends keyof SavedJobDraft>(key: K, value: SavedJobDraft[K]) {
    setDraft((current) => ({ ...current, [key]: value }));
  }

  return (
    <div className="page-stack">
      <header className="page-header">
        <div>
          <p className="eyebrow">Future applications</p>
          <h1>Saved jobs</h1>
        </div>
      </header>
      <section className="tool-panel">
        <h2>
          <Plus size={18} /> Add saved job
        </h2>
        <form className="form-grid" onSubmit={submit}>
          <label>
            Company
            <input required value={draft.company} onChange={(event) => update("company", event.target.value)} />
          </label>
          <label>
            Role
            <input required value={draft.role} onChange={(event) => update("role", event.target.value)} />
          </label>
          <label className="span-2">
            Job link
            <input value={draft.jobLink} onChange={(event) => update("jobLink", event.target.value)} />
          </label>
          <label>
            Skills
            <input value={draft.skills} onChange={(event) => update("skills", event.target.value)} />
          </label>
          <label>
            Tags
            <input value={draft.tags} onChange={(event) => update("tags", event.target.value)} />
          </label>
          <label className="span-2">
            Description
            <textarea rows={3} value={draft.description} onChange={(event) => update("description", event.target.value)} />
          </label>
          <button className="primary-button span-2" type="submit">
            Save job
          </button>
        </form>
      </section>
      {error && <p className="error-text">{error}</p>}
      <section className="record-list">
        {savedJobs.map((job) => (
          <article className="record-card" key={job.id}>
            <div>
              <strong>{job.role}</strong>
              <p>{job.company}</p>
              {job.jobLink && <a href={job.jobLink}>Open job</a>}
            </div>
            <div className="tag-row">
              {job.skills.map((skill) => (
                <span key={skill}>{skill}</span>
              ))}
              {job.tags.map((tag) => (
                <span key={tag}>{tag}</span>
              ))}
            </div>
          </article>
        ))}
      </section>
    </div>
  );
}
