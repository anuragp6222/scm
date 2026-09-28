# SCM Enhancements — Implementation Plan & Design Package (v0.1)

## Context
- Repo: https://github.com/anuragp6222/scm
- Base branch: `main`
- Planning branch: `planning/enhancement-plan-v0.1`
- Approved BA requirements revision: **v1**
- Approved design revision: **v0.1**
- Baseline commit SHA: **unknown** (design targets branch `main` as provided)

## Approved scope
- **FR-EDIT:** Edit contacts end-to-end (UI + service + persistence) + ownership enforcement; image replacement TBD.
- **FR-VIEW-API:** Contact details view + API hardened to return DTO + ownership enforcement.
- **FR-TEST:** Add Playwright smoke tests + minimal Spring tests.

## Baseline evidence (selected)
- Contact add/list/search: `scm2.0/src/main/java/com/scm/controller/ContactController.java`
- API endpoint returns entity: `scm2.0/src/main/java/com/scm/controller/ApiController.java`
- Contact update unimplemented: `scm2.0/src/main/java/com/scm/services/impl/ContactServiceImpl.java`
- Form validation: `scm2.0/src/main/java/com/scm/forms/ContactForm.java`
- Security: `scm2.0/src/main/java/com/scm/config/SecurityConfig.java`

## Open decisions (must confirm before implementation)
1. Unauthorized response: **404 vs 403** (UI + API)
2. Edit image behavior: keep unless replaced vs require image; delete old Cloudinary asset?
3. API auth policy for `/api/**`: require auth explicitly? unauthenticated behavior: 401 JSON vs redirect?
4. Playwright test data strategy: seeded SQL vs runtime creation vs test profile
5. Placement of View/Edit actions: list only vs list + details

## Architecture (summary)
Browser -> Spring Security -> Controllers -> Services -> JPA Repos -> MySQL
Image upload: Controller -> ImageService -> Cloudinary
Enhancements:
- Add contact detail and edit flows (MVC + Thymeleaf)
- Harden API to return `ContactDto` + enforce ownership
- Add Spring tests + Playwright smoke tests and reports

## Implementation Tasks (provisional; Jira creation pending)
### Phase 0: Baseline confirmation
- **PROV-TASK-00 (All):** Confirm decisions (1–5) and record in docs.

### Phase 1: Service + repo support
- **PROV-TASK-01 (FR-EDIT-02/04):** Implement `ContactService.update` semantics incl. ownership check.
- **PROV-TASK-02 (FR-EDIT-04, FR-VIEW-API-01):** Add `ContactRepo` finder by id+user.

### Phase 2: Contact details UI
- **PROV-TASK-03 (FR-VIEW-01):** Add `GET /user/contacts/{id}` endpoint with ownership checks.
- **PROV-TASK-04 (FR-VIEW-01):** Add template `user/contact_detail.html`.

### Phase 3: Edit contact UI
- **PROV-TASK-05 (FR-EDIT-01):** Add `GET /user/contacts/{id}/edit` endpoint; prefill form.
- **PROV-TASK-06 (FR-EDIT-02/03):** Add `POST /user/contacts/{id}/edit` update submission; image handling per decision.
- **PROV-TASK-07 (FR-EDIT-01):** Add template `user/edit_contact.html`.
- **PROV-TASK-08 (FR-EDIT-01, FR-VIEW-01):** Add View/Edit links to contacts list (and details if desired).

### Phase 4: API DTO hardening
- **PROV-TASK-09 (FR-VIEW-API-01):** Create `ContactDto` and mapper.
- **PROV-TASK-10 (FR-VIEW-API-01):** Update `/api/contacts/{id}` to return DTO + enforce ownership + apply `/api` auth policy.

### Phase 5: Testing
- **PROV-TASK-11 (FR-TEST-02):** Spring tests for update + ownership + DTO shape.
- **PROV-TASK-12 (FR-TEST-01):** Playwright scaffolding + smoke tests + HTML report output.
- **PROV-TASK-13 (FR-TEST-01):** Implement deterministic test data setup strategy.

### Phase 6: Build/run docs + rollback
- **PROV-TASK-14 (All):** Document local run steps; define rollback (revert commits; no DB migrations planned).

## Build & Run
- Backend tests: `cd scm2.0 && ./mvnw test`
- Run app: `cd scm2.0 && ./mvnw spring-boot:run`
- Playwright (planned): `npm ci` then `npx playwright test` (repo root)

## Risks
- Edit image validation may require form/validator change (`ContactForm` has `@ValidFile`).
- `/api/**` auth may be weaker than intended; must be made explicit after decision.
- DTO mapping must avoid entity recursion and overexposure.

## Traceability (FR -> tasks -> tests)
- FR-EDIT-01 → PROV-TASK-05/07/08 → Playwright navigation + Spring MVC GET edit
- FR-EDIT-02 → PROV-TASK-01/06 → Spring service update test + Playwright edit verify
- FR-EDIT-03 → PROV-TASK-06 → Spring test (image unchanged) if image optional
- FR-EDIT-04 → PROV-TASK-01/02/05/06 → Spring ownership tests
- FR-VIEW-01 → PROV-TASK-03/04/08 → Playwright view + Spring MVC GET details
- FR-VIEW-API-01 → PROV-TASK-02/09/10 → Spring MVC API contract tests
- FR-TEST-01 → PROV-TASK-12/13 → Playwright report artifact
- FR-TEST-02 → PROV-TASK-11 → Spring tests

## Approval record
- BA Requirements: **v1** — approved (user message: "approve")
- Design package: **v0.1** — approved (user message: "approved")
