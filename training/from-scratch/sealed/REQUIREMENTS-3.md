# Requirements, part 3 (the interviewer adds this near the end)

> "Two more things, and then let's talk about how you'd take this to production."

1. **Limit.** A wishlist holds at most **20** items. Adding a 21st → `422 Unprocessable Entity`.
2. **Share a wishlist.** `POST /travellers/{travellerId}/wishlist/share` → `201`
   ```json
   { "shareId": "…", "url": "/shared/…" }
   ```
   `GET /shared/{shareId}` → `200` with the traveller's **current** wishlist items (a live view, so items added
   later show up). Unknown share → `404`.
   Share ids go into public links, so they **must not be guessable** (no 1, 2, 3…).

Discussion (no code needed): what changes if this runs on 3 instances behind a load balancer? What would you
store in a database, and what indexes would you add? How would you stop someone scraping shared wishlists?
