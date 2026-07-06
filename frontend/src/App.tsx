import { AppShell } from "./components/AppShell";
import { ProtectedRoute } from "./components/ProtectedRoute";
import { ApplicationDetailPage } from "./pages/ApplicationDetailPage";
import { DashboardPage } from "./pages/DashboardPage";
import { ForgotPasswordPage } from "./pages/ForgotPasswordPage";
import { HomePage } from "./pages/HomePage";
import { LoginPage } from "./pages/LoginPage";
import { RegisterPage } from "./pages/RegisterPage";
import { ReviewPage } from "./pages/ReviewPage";
import { SavedJobsPage } from "./pages/SavedJobsPage";
import { SettingsEmailsPage } from "./pages/SettingsEmailsPage";
import { ResetPasswordPage } from "./pages/ResetPasswordPage";

export function App(): JSX.Element {
  const path = window.location.pathname;

  if (path === "/login") {
    return <LoginPage />;
  }
  if (path === "/register") {
    return <RegisterPage />;
  }
  if (path === "/forgot-password") {
    return <ForgotPasswordPage />;
  }
  if (path === "/reset-password") {
    return <ResetPasswordPage />;
  }

  return (
    <AppShell>
      {path === "/" && <HomePage />}
      {path === "/dashboard" && (
        <ProtectedRoute>
          <DashboardPage />
        </ProtectedRoute>
      )}
      {path === "/savedjobs" && (
        <ProtectedRoute>
          <SavedJobsPage />
        </ProtectedRoute>
      )}
      {path.startsWith("/applications/") && (
        <ProtectedRoute>
          <ApplicationDetailPage id={path.split("/").pop() ?? ""} />
        </ProtectedRoute>
      )}
      {path === "/review" && (
        <ProtectedRoute>
          <ReviewPage />
        </ProtectedRoute>
      )}
      {path === "/settings/emails" && (
        <ProtectedRoute>
          <SettingsEmailsPage />
        </ProtectedRoute>
      )}
    </AppShell>
  );
}
