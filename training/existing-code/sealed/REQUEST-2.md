# Interviewer, request 2

> "Families use the availability view, and they want to hide departures that can't fit their group.
> Can you add that?"

- `GET /tours/{id}/availability?date=2026-10-01&minSeats=4` → only departures with **at least** 4 seats remaining.
- Without `minSeats`, behaviour stays exactly as it is today (full departures are still listed, with `remaining: 0`).
- `minSeats` must be between 1 and 20. Anything else → `400`.

Expect follow-up questions from the "interviewer": *Is exactly 4 remaining included? Where should the filter run,
Java or SQL? How would you test it?*
