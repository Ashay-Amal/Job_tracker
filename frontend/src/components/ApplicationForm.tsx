import { useState } from "react";
import type { ApplicationStatus, JobApplication } from "../types";
import { joinList, splitList, STATUSES } from "../utils";

export interface ApplicationDraft {
  company: string;
  role: string;
  description: string;
  jobLink: string;
  status: ApplicationStatus;
  appliedDate: string;
  shortlisted: boolean;
  referred: boolean;
  referralSource: string;
  referralContact: string;
  referralEvidenceLink: string;
  referralEvidenceEmailId: string;
  notes: string;
  skills: string;
  tags: string;
}

interface ApplicationFormProps {
  initial?: JobApplication;
  submitLabel: string;
  onSubmit: (payload: unknown) => Promise<void>;
}

const emptyDraft: ApplicationDraft = {
  company: "",
  role: "",
  description: "",
  jobLink: "",
  status: "APPLIED",
  appliedDate: new Date().toISOString().slice(0, 10),
  shortlisted: false,
  referred: false,
  referralSource: "",
  referralContact: "",
  referralEvidenceLink: "",
  referralEvidenceEmailId: "",
  notes: "",
  skills: "",
  tags: ""
};

function toDraft(initial?: JobApplication): ApplicationDraft {
  if (!initial) {
    return emptyDraft;
  }
  return {
    company: initial.company,
    role: initial.role,
    description: initial.description ?? "",
    jobLink: initial.jobLink ?? "",
    status: initial.status,
    appliedDate: initial.appliedDate ?? "",
    shortlisted: initial.shortlisted,
    referred: initial.referred,
    referralSource: initial.referralSource ?? "",
    referralContact: initial.referralContact ?? "",
    referralEvidenceLink: initial.referralEvidenceLink ?? "",
    referralEvidenceEmailId: initial.referralEvidenceEmailId ?? "",
    notes: initial.notes ?? "",
    skills: joinList(initial.skills),
    tags: joinList(initial.tags)
  };
}

export function applicationPayload(draft: ApplicationDraft): unknown {
  return {
    ...draft,
    appliedDate: draft.appliedDate || null,
    skills: splitList(draft.skills),
    tags: splitList(draft.tags)
  };
}

export function ApplicationForm({ initial, submitLabel, onSubmit }: ApplicationFormProps): JSX.Element {
  const [draft, setDraft] = useState<ApplicationDraft>(() => toDraft(initial));
  const [saving, setSaving] = useState(false);

  async function submit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setSaving(true);
    try {
      await onSubmit(applicationPayload(draft));
      if (!initial) {
        setDraft(emptyDraft);
      }
    } finally {
      setSaving(false);
    }
  }

  function update<K extends keyof ApplicationDraft>(key: K, value: ApplicationDraft[K]) {
    setDraft((current) => ({ ...current, [key]: value }));
  }

  return (
    <form className="form-grid" onSubmit={submit}>
      <label>
        Company
        <input required value={draft.company} onChange={(event) => update("company", event.target.value)} />
      </label>
      <label>
        Role
        <input required value={draft.role} onChange={(event) => update("role", event.target.value)} />
      </label>
      <label>
        Status
        <select value={draft.status} onChange={(event) => update("status", event.target.value as ApplicationStatus)}>
          {STATUSES.map((status) => (
            <option key={status} value={status}>
              {status}
            </option>
          ))}
        </select>
      </label>
      <label>
        Applied date
        <input type="date" value={draft.appliedDate} onChange={(event) => update("appliedDate", event.target.value)} />
      </label>
      <label className="span-2">
        Job link
        <input value={draft.jobLink} onChange={(event) => update("jobLink", event.target.value)} />
      </label>
      <label className="span-2">
        Description
        <textarea rows={4} value={draft.description} onChange={(event) => update("description", event.target.value)} />
      </label>
      <label>
        Skills
        <input value={draft.skills} onChange={(event) => update("skills", event.target.value)} placeholder="react, java" />
      </label>
      <label>
        Tags
        <input value={draft.tags} onChange={(event) => update("tags", event.target.value)} placeholder="remote, internship" />
      </label>
      <div className="checkbox-row">
        <label>
          <input
            type="checkbox"
            checked={draft.shortlisted}
            onChange={(event) => update("shortlisted", event.target.checked)}
          />
          Shortlisted
        </label>
        <label>
          <input type="checkbox" checked={draft.referred} onChange={(event) => update("referred", event.target.checked)} />
          Referred
        </label>
      </div>
      {draft.referred && (
        <>
          <label>
            Referral source
            <input value={draft.referralSource} onChange={(event) => update("referralSource", event.target.value)} />
          </label>
          <label>
            Referral contact
            <input value={draft.referralContact} onChange={(event) => update("referralContact", event.target.value)} />
          </label>
          <label>
            Evidence link
            <input value={draft.referralEvidenceLink} onChange={(event) => update("referralEvidenceLink", event.target.value)} />
          </label>
          <label>
            Evidence email ID
            <input
              value={draft.referralEvidenceEmailId}
              onChange={(event) => update("referralEvidenceEmailId", event.target.value)}
            />
          </label>
        </>
      )}
      <label className="span-2">
        Notes
        <textarea rows={3} value={draft.notes} onChange={(event) => update("notes", event.target.value)} />
      </label>
      <button className="primary-button span-2" type="submit" disabled={saving}>
        {saving ? "Saving..." : submitLabel}
      </button>
    </form>
  );
}
