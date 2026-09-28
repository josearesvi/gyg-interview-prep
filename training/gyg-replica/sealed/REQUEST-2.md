# Interviewer, request 2 (≈ 12 min)

> "Search is broken: `/activities/search/Berlin` errors, and when it doesn't error it returns nothing.
> Users also type in lowercase. And supplier search returns *every* supplier when there's no match."

Expected (`Request2Test.java`):
- `GET /activities/search/{q}`: every activity whose title contains `q`, **case-insensitive**. No match → `[]`.
- `GET /suppliers/search/{q}`: **all** suppliers whose name, address, zip, city or country contains `q`
  (case-insensitive). No match → `[]`, never "all suppliers".
- Bonus to discuss: should the filtering happen in Java or in SQL? What about `%` or `_` in the search term?
