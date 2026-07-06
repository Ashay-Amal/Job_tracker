import { createContext, useContext, useEffect, useMemo, useState } from "react";
import { api, tokenStore } from "../api";
import type { AuthResponse, AuthUser } from "../types";

interface AuthContextValue {
  user: AuthUser | null;
  loading: boolean;
  login: (email: string, password: string) => Promise<void>;
  register: (email: string, password: string, fullName: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | null>(null);

function persistAuth(response: AuthResponse): AuthUser {
  tokenStore.accessToken = response.accessToken;
  tokenStore.refreshToken = response.refreshToken;
  localStorage.setItem("job-tracker-user", JSON.stringify(response.user));
  return response.user;
}

export function AuthProvider({ children }: { children: React.ReactNode }): JSX.Element {
  const [user, setUser] = useState<AuthUser | null>(() => {
    const raw = localStorage.getItem("job-tracker-user");
    return raw ? (JSON.parse(raw) as AuthUser) : null;
  });
  const [loading, setLoading] = useState(Boolean(tokenStore.accessToken));

  useEffect(() => {
    const hashParams = new URLSearchParams(window.location.hash.replace(/^#/, ""));
    const queryParams = new URLSearchParams(window.location.search);
    const params = hashParams.size > 0 ? hashParams : queryParams;
    const accessToken = params.get("accessToken");
    const refreshToken = params.get("refreshToken");
    if (accessToken && refreshToken) {
      tokenStore.accessToken = accessToken;
      tokenStore.refreshToken = refreshToken;
      window.history.replaceState({}, "", "/dashboard");
    }
  }, []);

  useEffect(() => {
    if (!tokenStore.accessToken) {
      setLoading(false);
      return;
    }
    let cancelled = false;
    api
      .me()
      .then((me) => {
        if (!cancelled) {
          setUser(me);
          localStorage.setItem("job-tracker-user", JSON.stringify(me));
        }
      })
      .catch(() => {
        if (!cancelled) {
          tokenStore.accessToken = null;
          tokenStore.refreshToken = null;
          localStorage.removeItem("job-tracker-user");
          setUser(null);
        }
      })
      .finally(() => {
        if (!cancelled) {
          setLoading(false);
        }
      });
    return () => {
      cancelled = true;
    };
  }, []);

  const value = useMemo<AuthContextValue>(
    () => ({
      user,
      loading,
      login: async (email, password) => {
        const response = await api.login(email, password);
        setUser(persistAuth(response));
      },
      register: async (email, password, fullName) => {
        const response = await api.register(email, password, fullName);
        setUser(persistAuth(response));
      },
      logout: () => {
        tokenStore.accessToken = null;
        tokenStore.refreshToken = null;
        localStorage.removeItem("job-tracker-user");
        setUser(null);
        window.location.href = "/";
      }
    }),
    [loading, user]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthContextValue {
  const value = useContext(AuthContext);
  if (!value) {
    throw new Error("useAuth must be used inside AuthProvider");
  }
  return value;
}
