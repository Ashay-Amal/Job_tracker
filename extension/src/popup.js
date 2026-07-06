const fields = {
  apiBaseUrl: document.querySelector("#apiBaseUrl"),
  email: document.querySelector("#email"),
  password: document.querySelector("#password"),
  company: document.querySelector("#company"),
  role: document.querySelector("#role"),
  jobLink: document.querySelector("#jobLink"),
  status: document.querySelector("#status"),
  skills: document.querySelector("#skills"),
  tags: document.querySelector("#tags"),
  description: document.querySelector("#description"),
  notes: document.querySelector("#notes")
};

const authView = document.querySelector("#auth-view");
const captureView = document.querySelector("#capture-view");
const message = document.querySelector("#message");
const accountEmail = document.querySelector("#accountEmail");

function splitList(value) {
  return value
    .split(",")
    .map((item) => item.trim().toLowerCase())
    .filter(Boolean);
}

function setMessage(value, isError = false) {
  message.textContent = value;
  message.style.color = isError ? "#932a18" : "#66756e";
}

async function getStorage(keys) {
  return chrome.storage.local.get(keys);
}

async function setStorage(values) {
  return chrome.storage.local.set(values);
}

async function apiRequest(path, options = {}) {
  const { apiBaseUrl, accessToken } = await getStorage(["apiBaseUrl", "accessToken"]);
  const response = await fetch(`${apiBaseUrl}${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(accessToken ? { Authorization: `Bearer ${accessToken}` } : {}),
      ...(options.headers || {})
    }
  });
  if (!response.ok) {
    const body = await response.json().catch(() => ({}));
    throw new Error(body.message || response.statusText);
  }
  return response.json();
}

function normalizeApiBaseUrl(value) {
  const url = new URL(value);
  const isLocal = url.hostname === "localhost" || url.hostname === "127.0.0.1";
  if (url.protocol !== "https:" && !(isLocal && url.protocol === "http:")) {
    throw new Error("Use HTTPS for remote API URLs.");
  }
  return url.origin;
}

async function render() {
  const { apiBaseUrl, accessToken, user } = await getStorage(["apiBaseUrl", "accessToken", "user"]);
  fields.apiBaseUrl.value = apiBaseUrl || "http://localhost:8080";
  authView.hidden = Boolean(accessToken);
  captureView.hidden = !accessToken;
  accountEmail.textContent = user?.email || "";
}

async function loadCurrentTab() {
  const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
  if (tab?.url) {
    fields.jobLink.value = tab.url;
  }
  if (tab?.title && !fields.role.value) {
    fields.role.value = tab.title.slice(0, 120);
  }
}

document.querySelector("#loginButton").addEventListener("click", async () => {
  setMessage("");
  try {
    await setStorage({ apiBaseUrl: normalizeApiBaseUrl(fields.apiBaseUrl.value) });
    const response = await apiRequest("/api/auth/login", {
      method: "POST",
      body: JSON.stringify({
        email: fields.email.value,
        password: fields.password.value
      })
    });
    await setStorage({
      accessToken: response.accessToken,
      refreshToken: response.refreshToken,
      user: response.user
    });
    fields.password.value = "";
    setMessage("Logged in.");
    await render();
  } catch (error) {
    setMessage(error.message || "Login failed", true);
  }
});

document.querySelector("#logoutButton").addEventListener("click", async () => {
  await chrome.storage.local.remove(["accessToken", "refreshToken", "user"]);
  setMessage("Logged out.");
  await render();
});

document.querySelector("#saveButton").addEventListener("click", async () => {
  setMessage("");
  try {
    const payload = {
      company: fields.company.value,
      role: fields.role.value,
      jobLink: fields.jobLink.value,
      description: fields.description.value,
      notes: fields.notes.value,
      skills: splitList(fields.skills.value),
      tags: splitList(fields.tags.value)
    };
    if (fields.status.value === "SAVED") {
      await apiRequest("/api/saved-jobs", {
        method: "POST",
        body: JSON.stringify(payload)
      });
    } else {
      await apiRequest("/api/applications", {
        method: "POST",
        body: JSON.stringify({
          ...payload,
          status: "APPLIED",
          appliedDate: new Date().toISOString().slice(0, 10),
          shortlisted: false,
          referred: false
        })
      });
    }
    setMessage("Saved to Job Tracker.");
  } catch (error) {
    setMessage(error.message || "Save failed", true);
  }
});

render().then(loadCurrentTab).catch((error) => setMessage(error.message, true));
