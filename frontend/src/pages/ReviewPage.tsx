import { useEffect, useState } from "react";
import { Check, X } from "lucide-react";
import { api } from "../api";
import type { ReviewSuggestion } from "../types";

export function ReviewPage(): JSX.Element {
  const [suggestions, setSuggestions] = useState<ReviewSuggestion[]>([]);
  const [error, setError] = useState("");

  async function load() {
    setSuggestions(await api.reviewSuggestions());
  }

  useEffect(() => {
    void load().catch((err) => setError(err instanceof Error ? err.message : "Unable to load review queue"));
  }, []);

  async function resolve(id: string, action: "approve" | "reject") {
    setError("");
    try {
      if (action === "approve") {
        await api.approveSuggestion(id);
      } else {
        await api.rejectSuggestion(id);
      }
      await load();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unable to update suggestion");
    }
  }

  return (
    <div className="page-stack">
      <header className="page-header">
        <div>
          <p className="eyebrow">Human-in-the-loop automation</p>
          <h1>Review queue</h1>
        </div>
      </header>
      {error && <p className="error-text">{error}</p>}
      <section className="record-list">
        {suggestions.map((suggestion) => (
          <article className="record-card review-card" key={suggestion.id}>
            <div>
              <strong>{suggestion.summary || suggestion.type}</strong>
              <p>{suggestion.evidenceSnippet || "No evidence snippet provided."}</p>
              <div className="tag-row">
                <span>{suggestion.type}</span>
                <span>{suggestion.confidenceBand}</span>
                <span>{Math.round(suggestion.confidence * 100)}%</span>
                {suggestion.proposedStatus && <span>{suggestion.proposedStatus}</span>}
                {suggestion.proposedReferred && <span>referred</span>}
              </div>
            </div>
            <div className="action-column">
              <button className="primary-button" type="button" onClick={() => void resolve(suggestion.id, "approve")}>
                <Check size={16} /> Approve
              </button>
              <button className="secondary-button" type="button" onClick={() => void resolve(suggestion.id, "reject")}>
                <X size={16} /> Reject
              </button>
            </div>
          </article>
        ))}
        {suggestions.length === 0 && <div className="notice">No pending suggestions.</div>}
      </section>
    </div>
  );
}
