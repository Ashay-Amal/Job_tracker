import type { ApplicationStatus } from "./types";

export const STATUSES: ApplicationStatus[] = [
  "SAVED",
  "APPLIED",
  "OA",
  "INTERVIEW",
  "OFFER",
  "REJECTED",
  "WITHDRAWN"
];

export function splitList(value: string): string[] {
  return value
    .split(",")
    .map((item) => item.trim().toLowerCase())
    .filter(Boolean);
}

export function joinList(values?: string[]): string {
  return values?.join(", ") ?? "";
}

export function formatDate(value?: string): string {
  if (!value) {
    return "Not set";
  }
  return new Intl.DateTimeFormat(undefined, { dateStyle: "medium" }).format(new Date(value));
}
