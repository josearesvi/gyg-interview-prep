# Requirements, part 1 (the interviewer reads this out at the start)

> "Travellers often find several activities they like before they're ready to book. We want a **wishlist**.
> Please build a small REST service for it. Keep it simple for now; in-memory or H2 is fine."

**Add an item.** `POST /travellers/{travellerId}/wishlist`
```json
{ "activityId": 123, "title": "Colosseum skip-the-line tour", "city": "Rome", "price": 49.90 }
```
→ `201 Created`, returning the stored item:
```json
{ "id": 1, "activityId": 123, "title": "Colosseum skip-the-line tour", "city": "Rome", "price": 49.90, "addedAt": "2026-10-01T10:00:00Z" }
```

**List a traveller's wishlist.** `GET /travellers/{travellerId}/wishlist` → `200` with a JSON array, in the order the
items were added. A traveller with no items gets `[]`.

**Remove an item.** `DELETE /travellers/{travellerId}/wishlist/{itemId}` → `204`. If that traveller has no such
item → `404`.

Rules:
- `travellerId` is any non-empty string. Each traveller only sees their own items.
- `activityId`, `title` and `city` are required, and `price` must be ≥ 0. Invalid input → `400`.
- Prices are EUR amounts with 2 decimals.
