# Requirements, part 2 (the interviewer adds this after part 1 works)

> "Nice! Product came back with some feedback from the first users…"

1. **No duplicates.** Adding an activity that is already on *that traveller's* wishlist → `409 Conflict`.
   (A different traveller can still add it.)
2. **Sorting.** `GET /travellers/{travellerId}/wishlist?sort=price_asc` or `sort=price_desc`.
   Without `sort`, keep the order the items were added. Any other value → `400`.
3. **Summary** for the wishlist header. `GET /travellers/{travellerId}/wishlist/summary` →
   ```json
   { "count": 2, "totalPrice": 99.80, "currency": "EUR" }
   ```
   `totalPrice` must be exact to the cent (0.10 + 0.20 is 0.30, not 0.30000000000000004).
   An empty wishlist → `count` 0, `totalPrice` 0.
