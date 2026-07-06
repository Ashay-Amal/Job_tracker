import type { Analytics } from "../types";

export function StatStrip({ analytics }: { analytics: Analytics | null }): JSX.Element {
  const stats = [
    ["Total", analytics?.totalCount ?? 0, ""],
    ["Applied", analytics?.appliedCount ?? 0, ""],
    ["Shortlisted", analytics?.shortlistedCount ?? 0, `${analytics?.shortlistedRate ?? 0}%`],
    ["OA", analytics?.oaCount ?? 0, `${analytics?.oaRate ?? 0}%`],
    ["Interview", analytics?.interviewCount ?? 0, `${analytics?.interviewRate ?? 0}%`],
    ["Referred", analytics?.referredCount ?? 0, ""]
  ];

  return (
    <section className="stat-grid" aria-label="Application analytics">
      {stats.map(([label, value, rate]) => (
        <div className="metric-card" key={label}>
          <span>{label}</span>
          <strong>{value}</strong>
          {rate ? <small>{rate}</small> : <small>filtered</small>}
        </div>
      ))}
    </section>
  );
}
