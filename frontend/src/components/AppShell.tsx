import { BarChart3, BriefcaseBusiness, Inbox, LogOut, Mail, SearchCheck } from "lucide-react";
import { useAuth } from "../state/AuthContext";

export function AppShell({ children }: { children: React.ReactNode }): JSX.Element {
  const { user, logout } = useAuth();

  return (
    <div className="app-frame">
      <aside className="sidebar">
        <a className="brand" href="/">
          <span className="brand-mark">JT</span>
          <span>
            <strong>Job Tracker</strong>
            <small>Private beta</small>
          </span>
        </a>
        <nav className="nav-list" aria-label="Main navigation">
          <a href="/dashboard">
            <BarChart3 size={18} /> Dashboard
          </a>
          <a href="/savedjobs">
            <BriefcaseBusiness size={18} /> Saved jobs
          </a>
          <a href="/review">
            <SearchCheck size={18} /> Review
          </a>
          <a href="/settings/emails">
            <Mail size={18} /> Emails
          </a>
        </nav>
        <div className="sidebar-footer">
          {user ? (
            <>
              <div className="account-chip">
                <Inbox size={16} />
                <span>{user.email}</span>
              </div>
              <button className="ghost-button" type="button" onClick={logout}>
                <LogOut size={16} /> Logout
              </button>
            </>
          ) : (
            <a className="primary-button full-width" href="/login">
              Login
            </a>
          )}
        </div>
      </aside>
      <main className="main-content">{children}</main>
    </div>
  );
}
