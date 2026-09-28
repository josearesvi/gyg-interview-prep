# Interviewer, request 1 (≈ 12 min)

> "Two tickets from the mobile team:
> 1. *'The activity detail page crashes for some activities, e.g. id 58820. Also, a deleted activity shows a
>    generic error instead of "not found".'*
> 2. *'The supplier list endpoint returns an enormous response that our JSON parser rejects.'*
>
> Can you look into both?"

Expected behaviour (the interviewer's hidden tests check it: `sealed-tests/.../Request1Test.java`):
- `GET /activities/{id}` → `200` for every existing activity, including ones whose supplier is missing
  (`supplierName` = `""`, as in the list endpoint).
- Unknown id → `404`. Non-numeric id (`/activities/abc`) → `400`.
- `GET /suppliers` → valid JSON, one object per supplier, **without** nested activities.
