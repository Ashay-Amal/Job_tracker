import { useEffect, useState } from "react";
import { Filter, Plus } from "lucide-react";
import { api } from "../api";
import { ApplicationForm } from "../components/ApplicationForm";
import { StatStrip } from "../components/StatStrip";
import type { Analytics, ApplicationStatus, JobApplication } from "../types";
import { formatDate, STATUSES } from "../utils";

interface Filters {
  month: string;
  year: string;
  company: string;
  role: string;
  skill: string;
  tag: string;
  status: string;
  referred: string;
  shortlisted: string;
}

const emptyFilters: Filters = {
  month: "",
  year: "",
  company: "",
  role: "",
  skill: "",
  tag: "",
  status: "",
  referred: "",
  shortlisted: ""
};

export function DashboardPage(): JSX.Element {
  const [filters, setFilters] = useState<Filters>(emptyFilters);
  const [applications, setApplications] = useState<JobApplication[]>([]);
  const [analytics, setAnalytics] = useState<Analytics | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [error, setError] = useState("");

  async function load() {
    setError("");
    try {
      const params = {
        ...filters,
        status: filters.status as ApplicationStatus | "",
        referred: filters.referred === "" ? undefined : filters.referred === "true",
        shortlisted: filters.shortlisted === "" ? undefined : filters.shortlisted === "true"
      };
      const [apps, stats] = await Promise.all([api.applications(params), api.analytics(params)]);
      setApplications(apps);
      setAnalytics(stats);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unable to load dashboard");
    }
  }

  useEffect(() => {
    void load();
  }, [filters]);

  function updateFilter<K extends keyof Filters>(key: K, value: Filters[K]) {
    setFilters((current) => ({ ...current, [key]: value }));
  }

  return (
    <div className="page-stack">
      <header className="page-header">
        <div>
          <p className="eyebrow">Application pipeline</p>
          <h1>Dashboard</h1>
        </div>
        <button className="primary-button" type="button" onClick={() => setShowForm((value) => !value)}>
          <Plus size={16} /> Add application
        </button>
      </header>

      <section className="filter-bar">
        <Filter size={18} />
        <input placeholder="Company" value={filters.company} onChange={(event) => updateFilter("company", event.target.value)} />
        <input placeholder="Role" value={filters.role} onChange={(event) => updateFilter("role", event.target.value)} />
        <input placeholder="Skill" value={filters.skill} onChange={(event) => updateFilter("skill", event.target.value)} />
        <input placeholder="Tag" value={filters.tag} onChange={(event) => updateFilter("tag", event.target.value)} />
        <input placeholder="Year" value={filters.year} onChange={(event) => updateFilter("year", event.target.value)} />
        <input placeholder="Month" value={filters.month} onChange={(event) => updateFilter("month", event.target.value)} />
        <select value={filters.status} onChange={(event) => updateFilter("status", event.target.value)}>
          <option value="">Any status</option>
          {STATUSES.map((status) => (
            <option key={status} value={status}>
              {status}
            </option>
          ))}
        </select>
        <select value={filters.referred} onChange={(event) => updateFilter("referred", event.target.value)}>
          <option value="">Referral</option>
          <option value="true">Referred</option>
          <option value="false">Not referred</option>
        </select>
        <button className="secondary-button" type="button" onClick={() => setFilters(emptyFilters)}>
          Clear
        </button>
      </section>

      <StatStrip analytics={analytics} />

      {showForm && (
        <section className="tool-panel">
          <h2>Add application</h2>
          <ApplicationForm
            submitLabel="Save application"
            onSubmit={async (payload) => {
              await api.createApplication(payload);
              setShowForm(false);
              await load();
            }}
          />
        </section>
      )}

      {error && <p className="error-text">{error}</p>}

      <section className="record-list">
        {applications.map((application) => (
          <article className="record-card" key={application.id}>
            <div>
              <a className="record-title" href={`/applications/${application.id}`}>
                {application.role}
              </a>
              <p>{application.company}</p>
              <small>Applied: {formatDate(application.appliedDate)}</small>
            </div>
            <div className="tag-row">
              <span className="status-pill">{application.status}</span>
              {application.shortlisted && <span>shortlisted</span>}
              {application.referred && <span>referred</span>}
              {application.skills.map((skill) => (
                <span key={skill}>{skill}</span>
              ))}
            </div>
          </article>
        ))}
        {applications.length === 0 && <div className="notice">No applications match the current filters.</div>}
      </section>
    </div>
  );
}
