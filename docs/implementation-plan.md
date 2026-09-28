# SCM Capstone — Implementation Plan (Edit/Delete Contacts + Secure Contact API)

Status: Design Draft v0.1 (approved)

## Goals

- Enable editing and deleting contacts in the UI.
- Secure the contact API so only authenticated users can access it.
- Maintain current UX patterns and coding conventions.
- Provide a clear rollout plan with tests and rollback steps.

## Evidence / Context

- Current UI supports listing and creating contacts but lacks edit/delete flows.
- The contact API is accessible without authentication, which is not acceptable for production.
- The codebase already has authentication primitives that should be reused.

## Open Decisions

- Confirm whether deletes should be soft-delete or hard-delete (default: hard-delete).
- Confirm whether edit form should be modal or dedicated route (default: dedicated route to match existing patterns).
- Confirm audit logging requirements (default: log request metadata + contact id).

## Work Plan

### Phase 0 — Prep & Alignment

- Review existing contact model, API routes, and UI components.
- Confirm auth strategy and required scopes/roles.
- Add tracking issue(s) and define acceptance criteria.

### Phase 1 — Secure Contact API

- Require authentication for all `/api/contacts` endpoints.
- Ensure consistent 401/403 responses.
- Add/update middleware/guards to align with existing auth conventions.
- Add tests for unauthenticated and unauthorized access.

### Phase 2 — Edit Contact (API + UI)

- API
  - Add/confirm `PUT /api/contacts/:id` (or `PATCH`) to update fields.
  - Validate payload and return updated contact.
  - Handle 404 and validation errors consistently.
- UI
  - Add "Edit" action on contact rows.
  - Implement edit form with existing form components/validation patterns.
  - Add optimistic UI updates or refetch on success (match existing data-fetching pattern).
  - Display inline/server validation errors.

### Phase 3 — Delete Contact (API + UI)

- API
  - Add/confirm `DELETE /api/contacts/:id`.
  - Return 204 on success.
  - Handle 404 consistently.
- UI
  - Add "Delete" action on contact rows.
  - Add confirmation dialog (required).
  - Ensure list refreshes and errors are surfaced to the user.

### Phase 4 — QA, Docs, Rollout

- Run full test suite and fix regressions.
- Update docs and any relevant README snippets.
- Prepare deployment checklist and rollback plan.

## Test Plan

- Unit tests
  - API auth: unauthenticated -> 401; unauthorized -> 403.
  - API update: valid payload -> 200 with updated record; invalid -> 400/422; missing -> 404.
  - API delete: existing -> 204; missing -> 404.
- Integration/E2E
  - User can edit a contact and sees updates in the list.
  - User can delete a contact after confirming and it disappears from the list.
  - Unauthenticated users cannot access contacts pages or APIs.

## Build & Run

- Install dependencies as usual for the repo.
- Start backend and frontend per existing scripts.
- Verify API endpoints with an authenticated session.

## Rollback

- If issues occur post-deploy:
  - Revert the deployment to the previous release.
  - Roll back database migrations if any were applied.
  - Disable edit/delete UI actions behind a feature flag if available.
  - Monitor logs for auth failures and API errors.
