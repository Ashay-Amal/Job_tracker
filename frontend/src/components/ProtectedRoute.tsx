import { useAuth } from "../state/AuthContext";

export function ProtectedRoute({ children }: { children: React.ReactNode }): JSX.Element {
  const { user, loading } = useAuth();

  if (loading) {
    return <div className="notice">Loading your tracker...</div>;
  }

  if (!user) {
    return (
      <div className="empty-state">
        <h1>Login required</h1>
        <p>Use your Job Tracker account to open this workspace.</p>
        <a className="primary-button" href="/login">
          Login
        </a>
      </div>
    );
  }

  return <>{children}</>;
}
