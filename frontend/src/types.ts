export type ApplicationStatus =
  | "SAVED"
  | "APPLIED"
  | "OA"
  | "INTERVIEW"
  | "OFFER"
  | "REJECTED"
  | "WITHDRAWN";

export interface AuthUser {
  id: string;
  email: string;
  fullName: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  user: AuthUser;
}

export interface JobApplication {
  id: string;
  company: string;
  role: string;
  description?: string;
  jobLink?: string;
  status: ApplicationStatus;
  appliedDate?: string;
  shortlisted: boolean;
  referred: boolean;
  referralSource?: string;
  referralContact?: string;
  referralEvidenceLink?: string;
  referralEvidenceEmailId?: string;
  notes?: string;
  skills: string[];
  tags: string[];
  createdAt: string;
  updatedAt: string;
}

export interface SavedJob {
  id: string;
  company: string;
  role: string;
  jobLink?: string;
  description?: string;
  notes?: string;
  skills: string[];
  tags: string[];
  createdAt: string;
  updatedAt: string;
}

export interface Analytics {
  totalCount: number;
  appliedCount: number;
  shortlistedCount: number;
  shortlistedRate: number;
  oaCount: number;
  oaRate: number;
  interviewCount: number;
  interviewRate: number;
  offerCount: number;
  rejectedCount: number;
  referredCount: number;
}

export interface ReviewSuggestion {
  id: string;
  targetApplicationId?: string;
  type: "STATUS_UPDATE" | "REFERRAL_DETECTED" | "LABEL_SUGGESTION" | "APPLICATION_MATCH";
  state: "PENDING" | "APPROVED" | "REJECTED";
  confidenceBand: "LOW" | "MEDIUM" | "HIGH";
  confidence: number;
  summary?: string;
  evidenceSnippet?: string;
  evidenceEmailId?: string;
  proposedStatus?: ApplicationStatus;
  proposedReferred?: boolean;
  proposedReferralSource?: string;
  proposedReferralContact?: string;
  proposedSkills: string[];
  createdAt: string;
  resolvedAt?: string;
}

export interface ConnectedEmail {
  id: string;
  emailAddress: string;
  displayName?: string;
  provider: string;
  active: boolean;
  connectedAt: string;
  lastSyncedAt?: string;
}
