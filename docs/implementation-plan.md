# SCM Enhancements Implementation Plan (v0.2-draft)

## 1. Overview

This document defines the approved implementation plan for the SCM enhancements scope. It is intended to be used as the single source of truth for the work breakdown, decision gates, and test coverage expectations. This plan is documentation-only and does not include implementation code.

### 1.1 References

- Confluence: “Implementation plan (docs/implementation-plan.md content)”
- Baseline branch: `main` (commit SHA: TBD)
- Planning branch (docs-only PR): `planning/enhancement-plan`
- Jira: unavailable (all IDs are provisional `DRAFT-*`; creation pending)

---

## 2. Scope (approved)

1. **Contact export to CSV** (authenticated users).
2. **Security hardening**: enable CSRF and ensure logout continues to work.
3. **Portability/onboarding**: add README and fix Tailwind build script paths.

### 2.1 Non-goals (out of scope)

- Major UI redesign or SPA conversion.
- New database/migration framework.
- Production deployment pipeline changes.

---

## 3. Baseline evidence (key files)

- Contacts controller: `scm2.0/src/main/java/com/scm/controller/ContactController.java`
- Contact service/repo: `scm2.0/src/main/java/com/scm/services/ContactService.java`, `scm2.0/src/main/java/com/scm/repositories/ContactRepo.java`
- Security config: `scm2.0/src/main/java/com/scm/config/SecurityConfig.java` (CSRF disabled; logout configured)
- Templates: `scm2.0/src/main/resources/templates/user/contacts.html`, `.../search.html`, `.../add_contact.html`
- Tailwind: root `package.json` (absolute paths), `scm2.0/tailwind.config.js`, `static/css/input.css`, `static/css/output.css`

---

## 4. Decision gates (must confirm before coding)

- **DRAFT-TASK-DEC-01**: Export behavior — export all contacts vs export filtered by search query.
- **DRAFT-TASK-DEC-02**: CSV columns — minimum required fields; confirm whether to include description and links.
- **DRAFT-TASK-DEC-03**: CSRF exceptions — any endpoints (e.g., actuator/APIs) that require CSRF ignore.
- **DRAFT-TASK-DEC-04**: Tailwind contract — confirm compiled CSS output path used by templates.

---

## 5. Implementation tasks (ordered)

### 5.1 Enhancement 1 — CSV export

- **DRAFT-TASK-01 (DRAFT-STORY-01)**: Identify data retrieval strategy for export.
  - Prefer repository `findByUserId` for all-contacts export.
  - Define approach for filtered export if approved by decision gate DRAFT-TASK-DEC-01.
- **DRAFT-TASK-02 (DRAFT-STORY-01)**: Add endpoint `GET /user/contacts/export.csv` returning `text/csv` with an attachment filename.
- **DRAFT-TASK-03 (DRAFT-STORY-01)**: Implement CSV formatting utility:
  - Escape commas/quotes/newlines correctly.
  - Decide on CSV injection hardening (e.g., prefix risky leading characters).
- **DRAFT-TASK-04 (DRAFT-STORY-01)**: Add “Export CSV” button/link on contacts list template.
- **DRAFT-TASK-04b (DRAFT-STORY-02, if approved)**: Add “Export CSV” button/link on search results template preserving filter params.
- **DRAFT-TASK-05 (DRAFT-STORY-01/02)**: Add MVC tests for export:
  - Authenticated => 200 + header row.
  - User scoping (no cross-user leakage).
  - Empty contacts => header-only CSV.
  - Filtered export => only matches (if approved).

### 5.2 Enhancement 2 — CSRF + logout

- **DRAFT-TASK-06 (DRAFT-STORY-03)**: Enable CSRF in `SecurityConfig` (remove disable).
- **DRAFT-TASK-07 (DRAFT-STORY-03)**: Update all Thymeleaf POST forms to include CSRF token:
  - Add contact, register, any delete/update forms, logout form if POST.
- **DRAFT-TASK-08 (DRAFT-STORY-04)**: Update logout UI and configuration to work with CSRF enabled (prefer POST logout).
- **DRAFT-TASK-08b (DRAFT-STORY-03/04)**: Add security regression tests:
  - POST without CSRF => 403.
  - POST with CSRF => succeeds (at least one representative endpoint).

### 5.3 Enhancement 3 — README + Tailwind portability

- **DRAFT-TASK-09 (DRAFT-STORY-05)**: Add root `README.md`:
  - Prerequisites, config keys (no secrets), DB setup, run steps, test steps.
- **DRAFT-TASK-10 (DRAFT-STORY-06)**: Update root `package.json` scripts to use relative paths for Tailwind input/output.
- **DRAFT-TASK-11 (DRAFT-STORY-06)**: Ensure `scm2.0/tailwind.config.js` content globs align with templates and JS; adjust command to reference correct config path.
- **DRAFT-TASK-12 (DRAFT-STORY-06)**: Document CSS build (watch/build) in README.

---

## 6. QA plan — Playwright (capstone)

- **DRAFT-QA-01**: Add Playwright scaffolding + scripts + HTML report output.
- **DRAFT-QA-02**: E2E: login -> contacts -> export download -> validate CSV header.
- **DRAFT-QA-03**: E2E: unauthenticated export attempt -> redirected/unauthorized.
- **DRAFT-QA-04**: Validate CSRF enforcement (no token => rejected).
- **DRAFT-QA-05**: Ensure Playwright report is archived and referenced in PR.

---

## 7. Build & run (local)

### 7.1 CSS (from repo root)

- `npm ci`
- `npm run build:css` (or watch variant)

### 7.2 App (from `scm2.0/`)

- `./mvnw spring-boot:run`

### 7.3 Unit tests (from `scm2.0/`)

- `./mvnw test`

### 7.4 E2E tests (planned, from repo root)

After starting DB + app:

- `npx playwright test --reporter=html`

---

## 8. Rollback plan & risks

### 8.1 Rollback plan

- CSV export: remove endpoint and UI link; no DB changes expected.
- CSRF: revert security config and template token changes if required (not recommended long-term).
- Tailwind scripts: revert `package.json` scripts if build process breaks; compiled CSS output remains versioned.

### 8.2 Risks

- CSRF may break existing forms if any are missed.
- Logout behavior changes with CSRF; must test.
- Export filtered vs all must be decided before implementation.
- Tailwind working directory/config path mismatch can break CSS rebuild.
