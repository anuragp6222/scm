# SCM Capstone — Implementation Plan (Approved Requirements v1)
Design Revision: v0.1 (draft)
Baseline Branch: main (commit SHA not captured in baseline inspection)
Confluence Space: EP
Design Parent Page: 4653057
Planning Branch (for docs-only PR): planning/enhancement-plan
Jira: unavailable (all IDs are provisional DRAFT-*; creation pending)

## Scope (approved)
1. Contact export to CSV (authenticated users).
2. Security hardening: enable CSRF + ensure logout continues to work.
3. Portability/onboarding: add README + fix Tailwind build script paths.

## Non-goals (out of scope)
- Major UI redesign, SPA conversion.
- New database/migration framework.
- Production deployment pipeline changes.

---

## Observed baseline evidence (key files)
- Contacts controller: `scm2.0/src/main/java/com/scm/controller/ContactController.java`
- Contact service/repo: `scm2.0/src/main/java/com/scm/services/ContactService.java`, `scm2.0/src/main/java/com/scm/repositories/ContactRepo.java`
- Security config: `scm2.0/src/main/java/com/scm/config/SecurityConfig.java` (CSRF disabled; logout configured)
- Templates: `scm2.0/src/main/resources/templates/user/contacts.html`, `.../search.html`, `.../add_contact.html`
- Tailwind: root `package.json` (absolute paths), `scm2.0/tailwind.config.js`, `static/css/input.css`, `static/css/output.css`

---

## Decision Gates (must confirm before coding)
- DRAFT-TASK-DEC-01: Export behavior — all contacts vs filtered by search query.
- DRAFT-TASK-DEC-02: CSV columns — minimum required fields and whether to include description and links.
- DRAFT-TASK-DEC-03: CSRF exceptions — any endpoints (e.g., actuator/APIs) needing CSRF ignore.
- DRAFT-TASK-DEC-04: Tailwind contract — confirm compiled CSS output path used by templates.

---

## Implementation Tasks (ordered)

### Enhancement 1 — CSV Export
- DRAFT-TASK-01 (DRAFT-STORY-01): Identify data retrieval for export.
  - Prefer repo `findByUserId` for all-contacts export; define approach for filtered export if approved.
- DRAFT-TASK-02 (DRAFT-STORY-01): Add endpoint `GET /user/contacts/export.csv` returning `text/csv` with attachment filename.
- DRAFT-TASK-03 (DRAFT-STORY-01): Implement CSV formatting utility (escape commas/quotes/newlines; decide CSV injection hardening).
- DRAFT-TASK-04 (DRAFT-STORY-01): Add “Export CSV” button/link on contacts list template.
- DRAFT-TASK-04b (DRAFT-STORY-02, if approved): Add “Export CSV” button/link on search results template preserving filter params.
- DRAFT-TASK-05 (DRAFT-STORY-01/02): Add MVC tests for export:
  - authenticated => 200 + header row
  - user scoping (no cross-user leakage)
  - empty contacts => header-only CSV
  - filtered export => only matches (if approved)

### Enhancement 2 — CSRF + Logout
- DRAFT-TASK-06 (DRAFT-STORY-03): Enable CSRF in `SecurityConfig` (remove disable).
- DRAFT-TASK-07 (DRAFT-STORY-03): Update all Thymeleaf POST forms to include CSRF token:
  - add contact, register, any delete/update forms, logout form if POST.
- DRAFT-TASK-08 (DRAFT-STORY-04): Update logout UI and config to work with CSRF enabled (prefer POST logout).
- DRAFT-TASK-08b (DRAFT-STORY-03/04): Add security regression tests:
  - POST without CSRF => 403
  - POST with CSRF => succeeds (at least one representative endpoint)

### Enhancement 3 — README + Tailwind portability
- DRAFT-TASK-09 (DRAFT-STORY-05): Add root `README.md`:
  - prerequisites, config keys (no secrets), DB setup, run steps, test steps.
- DRAFT-TASK-10 (DRAFT-STORY-06): Update root `package.json` scripts to use relative paths for tailwind input/output.
- DRAFT-TASK-11 (DRAFT-STORY-06): Ensure `scm2.0/tailwind.config.js` content globs align with templates and JS; adjust command to reference correct config path.
- DRAFT-TASK-12 (DRAFT-STORY-06): Document CSS build (watch/build) in README.

### QA — Playwright (capstone)
- DRAFT-QA-01: Add Playwright scaffolding + scripts + HTML report output.
- DRAFT-QA-02: E2E: login -> contacts -> export download -> validate CSV header.
- DRAFT-QA-03: E2E: unauthenticated export attempt -> redirected/unauthorized.
- DRAFT-QA-04: Validate CSRF enforcement (no token => rejected).
- DRAFT-QA-05: Ensure Playwright report archived and referenced in PR.

---

## Build & Run (local)
### CSS
From repo root:
- `npm ci`
- `npm run build:css` (or watch variant)

### App
From `scm2.0/`:
- `./mvnw spring-boot:run`

### Unit tests
From `scm2.0/`:
- `./mvnw test`

### E2E tests (planned)
From repo root (after starting DB + app):
- `npx playwright test --reporter=html`

---

## Rollback plan
- CSV export: remove endpoint + UI link; no DB changes expected.
- CSRF: revert security config and template token changes if required (not recommended long-term).
- Tailwind scripts: revert `package.json` scripts if build process breaks; output CSS remains versioned.

---

## Risks
- CSRF may break existing forms if any are missed.
- Logout behavior changes with CSRF; must test.
- Export filtered vs all must be decided before implementation.
- Tailwind working directory/config path mismatch can break CSS rebuild.

---

## Traceability
(See planning/design package traceability matrix: DRAFT-STORY-01..06 mapped to tasks and tests.)
