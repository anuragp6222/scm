# SCM Enhancements — Implementation Plan (Design Draft v0.1)

Status: Draft (awaiting human review)  
Baseline: repo https://github.com/anuragp6222/scm, branch `main` (commit SHA not available via tools)  
Approved requirements: Requirements revision v1 (approval: "Approve")  
Scope: Enhancements A (Edit/Update), B (Secure DTO API + JS fix), C (Playwright + runbook)

## 1. Goals
1. Add Contact Edit/Update UI flow with ownership enforcement.
2. Secure `/api/contacts/{id}`:
   - enforce owner-only access
   - return DTO (no JPA entity exposure)
   - update frontend to use relative URL (no localhost hard-code)
3. Add Playwright E2E smoke tests and a minimal local run/test runbook.

## 2. Evidence (current baseline)
- MVC add/list/search contacts: `scm2.0/src/main/java/com/scm/controller/ContactController.java`
- REST returns Contact entity: `scm2.0/src/main/java/com/scm/controller/ApiController.java`
- JS hard-codes localhost: `scm2.0/src/main/resources/static/js/Contacts.js`
- Update method unimplemented: `scm2.0/src/main/java/com/scm/services/impl/ContactServiceImpl.java`
- Contact form validation: `scm2.0/src/main/java/com/scm/forms/ContactForm.java`
- CSRF disabled: `scm2.0/src/main/java/com/scm/config/SecurityConfig.java`

## 3. Open decisions (require confirmation)
1. Non-owner access response: 404 (proposed) vs 403.
2. CSRF: keep disabled (proposed) vs enable.
3. Playwright image upload: skip (proposed) vs real upload vs mock.
4. Edit URL: `/user/contacts/{id}/edit` (proposed) vs query-param.
5. Test user strategy: seeded user (proposed) vs register via UI.

## 4. Work plan (ordered)
### Phase 0 — Planning
- TASK-P0.1: Jira Epic/Stories creation (blocked; Jira access unavailable). Use provisional IDs:
  - DRAFT-STORY-01..05
- TASK-P0.2: Confirm open decisions.

### Phase 1 — Secure DTO API (Enhancement B)
- TASK-B1: Create `ContactDto` and mapping.
- TASK-B2: Update `GET /api/contacts/{id}` to:
  - require authenticated user context
  - enforce ownership
  - return DTO
- TASK-B3 (recommended): Add repo method `findByIdAndUser(id, user)` and use it.

### Phase 2 — Edit/Update flow (Enhancement A)
- TASK-A1: Implement `ContactServiceImpl.update(Contact)`.
- TASK-A2: Add MVC endpoints:
  - GET `/user/contacts/{id}/edit`
  - POST `/user/contacts/{id}/edit`
- TASK-A3: Add Thymeleaf template `user/edit_contact.html` (or refactor reuse).
- TASK-A4: Add "Edit" action/link in contacts list.

### Phase 3 — JS fix (Enhancement B)
- TASK-F1: Update `Contacts.js` to call `/api/contacts/${id}` and handle non-OK responses.
- TASK-F2 (optional): Add modal error placeholder in `contacts_modals.html`.

### Phase 4 — Playwright + runbook (Enhancement C)
- TASK-T1: Add Playwright config and dependencies.
- TASK-T2: Add smoke tests:
  1) login
  2) add contact (text-only)
  3) open modal and assert populated fields
- TASK-T3: Add docs: `docs/local-run.md` describing:
  - MySQL prerequisite
  - env overrides (no secrets committed)
  - how to run app + Playwright, how to view report

## 5. Test plan
- Unit/Service (optional): ownership lookup and DTO mapping.
- Playwright (required):
  - produces HTML report (default `playwright-report/`)
- Manual QA:
  - edit validation
  - non-owner access attempt to API and edit endpoints

## 6. Build & run (planned)
Backend:
- `cd scm2.0`
- `./mvnw clean test`
- `./mvnw spring-boot:run`

Playwright:
- `npm ci`
- `npx playwright install --with-deps`
- `npx playwright test`
- `npx playwright show-report`

## 7. Rollback
- Revert feature commits on the feature branch.
- No DB migrations anticipated for this scope.
