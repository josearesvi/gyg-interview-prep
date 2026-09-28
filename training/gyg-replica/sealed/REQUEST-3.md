# Interviewer, request 3 (≈ 12 min)

> "Product wants a supplier dashboard. There's a `/suppliers/stats` endpoint, but it doesn't return statistics.
> Please make it useful."

Expected (`Request3Test.java`): `GET /suppliers/stats` → a JSON array, one object per supplier that has activities,
sorted by `activityCount` descending:
```json
{ "supplierName": "City Pass Berlin", "activityCount": 5, "totalRevenue": 233, "averageRating": 4.54 }
```
- `totalRevenue` = sum of the activity prices. `averageRating` = the mean rating rounded to 2 decimals.
- Discuss: JPQL `GROUP BY` into a DTO vs `List<Object[]>`, where the DTO class should live, and what happens if the
  constructor-expression argument order doesn't match the DTO.
