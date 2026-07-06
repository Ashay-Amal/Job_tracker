import { ArrowRight, CheckCircle2, Chrome, Mail, ShieldCheck } from "lucide-react";

export function HomePage(): JSX.Element {
  return (
    <div className="home-layout">
      <section className="hero-band">
        <div>
          <p className="eyebrow">Gmail + extension + reviewable AI</p>
          <h1>Job Tracker</h1>
          <p className="hero-copy">
            Track applications across multiple emails, manual referrals, saved roles, status updates, and skill labels without
            letting automation silently overwrite your pipeline.
          </p>
          <div className="button-row">
            <a className="primary-button" href="/dashboard">
              Open dashboard <ArrowRight size={16} />
            </a>
            <a className="secondary-button" href="/register">
              Create account
            </a>
          </div>
        </div>
        <div className="product-preview" aria-label="Dashboard preview">
          <div className="preview-toolbar">
            <span />
            <span />
            <span />
          </div>
          <div className="preview-grid">
            <div>
              <strong>Applications</strong>
              <p>Latest first, filtered by company, role, skills, tags, referral, and status.</p>
            </div>
            <div>
              <strong>Review queue</strong>
              <p>Gmail and AI findings become confirmable suggestions with evidence.</p>
            </div>
            <div>
              <strong>Analytics</strong>
              <p>Applied, shortlisted, OA, interview, referral, and conversion rates update with filters.</p>
            </div>
          </div>
        </div>
      </section>

      <section className="feature-row">
        <article>
          <Mail size={22} />
          <h2>Multi-email tracking</h2>
          <p>Connect every Gmail account involved in applications and updates.</p>
        </article>
        <article>
          <Chrome size={22} />
          <h2>Manual extension capture</h2>
          <p>Save or mark jobs as applied from any browser account.</p>
        </article>
        <article>
          <ShieldCheck size={22} />
          <h2>Confidence-first automation</h2>
          <p>Status and referral updates wait for review before changing records.</p>
        </article>
        <article>
          <CheckCircle2 size={22} />
          <h2>Editable records</h2>
          <p>Correct missed labels, referral evidence, dates, and statuses anytime.</p>
        </article>
      </section>
    </div>
  );
}
