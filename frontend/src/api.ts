import type {
  Analytics,
  AuthResponse,
  ConnectedEmail,
  JobApplication,
  ReviewSuggestion,
  SavedJob
} from "./types";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080";

export class ApiError extends Error {
  constructor(
    message: string,
    public readonly status: number
  ) {
    super(message);
  }
}

export const tokenStore = {
  get accessToken(): string | null {
    return localStorage.getItem("job-tracker-access-token");
  },
  set accessToken(value: string | null) {
    if (value) {
      localStorage.setItem("job-tracker-access-token", value);
    } else {
      localStorage.removeItem("job-tracker-access-token");
    }
  },
  get refreshToken(): string | null {
    return localStorage.getItem("job-tracker-refresh-token");
  },
  set refreshToken(value: string | null) {
    if (value) {
      localStorage.setItem("job-tracker-refresh-token", value);
    } else {
      localStorage.removeItem("job-tracker-refresh-token");
    }
  }
};

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const headers = new Headers(options.headers);
  headers.set("Content-Type", "application/json");
  if (tokenStore.accessToken) {
    headers.set("Authorization", `Bearer ${tokenStore.accessToken}`);
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers
  });

  if (!response.ok) {
    let message = response.statusText;
    try {
      const body = (await response.json()) as { message?: string };
      message = body.message ?? message;
    } catch {
      // Keep the HTTP status text when the response is not JSON.
    }
    throw new ApiError(message, response.status);
  }

  if (response.status === 204) {
    return undefined as T;
  }
  return (await response.json()) as T;
}

export function queryString(params: Record<string, string | number | boolean | undefined>): string {
  const search = new URLSearchParams();
  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== "") {
      search.set(key, String(value));
    }
  });
  const value = search.toString();
  return value ? `?${value}` : "";
}

export const api = {
  login: (email: string, password: string) =>
    request<AuthResponse>("/api/auth/login", {
      method: "POST",
      body: JSON.stringify({ email, password })
    }),
  register: (email: string, password: string, fullName: string) =>
    request<AuthResponse>("/api/auth/register", {
      method: "POST",
      body: JSON.stringify({ email, password, fullName })
    }),
  forgotPassword: (email: string) =>
    request<{ message: string }>("/api/auth/forgot-password", {
      method: "POST",
      body: JSON.stringify({ email })
    }),
  resetPassword: (token: string, newPassword: string) =>
    request<{ message: string }>("/api/auth/reset-password", {
      method: "POST",
      body: JSON.stringify({ token, newPassword })
    }),
  me: () => request<{ id: string; email: string; fullName: string }>("/api/auth/me"),
  applications: (params: Record<string, string | number | boolean | undefined>) =>
    request<JobApplication[]>(`/api/applications${queryString(params)}`),
  application: (id: string) => request<JobApplication>(`/api/applications/${id}`),
  createApplication: (payload: unknown) =>
    request<JobApplication>("/api/applications", {
      method: "POST",
      body: JSON.stringify(payload)
    }),
  updateApplication: (id: string, payload: unknown) =>
    request<JobApplication>(`/api/applications/${id}`, {
      method: "PUT",
      body: JSON.stringify(payload)
    }),
  analytics: (params: Record<string, string | number | boolean | undefined>) =>
    request<Analytics>(`/api/analytics${queryString(params)}`),
  savedJobs: () => request<SavedJob[]>("/api/saved-jobs"),
  createSavedJob: (payload: unknown) =>
    request<SavedJob>("/api/saved-jobs", {
      method: "POST",
      body: JSON.stringify(payload)
    }),
  reviewSuggestions: () => request<ReviewSuggestion[]>("/api/review-suggestions"),
  approveSuggestion: (id: string) =>
    request<ReviewSuggestion>(`/api/review-suggestions/${id}/approve`, { method: "POST" }),
  rejectSuggestion: (id: string) =>
    request<ReviewSuggestion>(`/api/review-suggestions/${id}/reject`, { method: "POST" }),
  connectedEmails: () => request<ConnectedEmail[]>("/api/connected-emails"),
  createConnectedEmail: (payload: { emailAddress: string; displayName?: string }) =>
    request<ConnectedEmail>("/api/connected-emails", {
      method: "POST",
      body: JSON.stringify(payload)
    }),
  syncGmail: (connectedEmailId: string) =>
    request<{ scannedMessages: number; suggestionsCreated: number; message: string }>("/api/gmail/sync", {
      method: "POST",
      body: JSON.stringify({ connectedEmailId })
    })
};
