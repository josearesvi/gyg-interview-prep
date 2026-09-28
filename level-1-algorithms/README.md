# Level 1: Algorithms and data structures (plain Java, no Spring)

Covers the doc's **"Algorithms: sorting, searching, recursion"** and **"Data structures: arrays, hashmaps,
trees, graphs"**. All seven exercises use travel-marketplace data, the way GetYourGuide would frame them.

| # | File | Topic | Difficulty | Target |
|---|------|-------|-----------|--------|
| 1 | `E1_TopRatedActivities` | Sorting, comparators, heap | Easy | O(n log k) |
| 2 | `E2_PriceRangeSearch` | Binary search (lower/upper bound) | Easy–Med | O(log n) |
| 3 | `E3_TimeSlotMerger` | Sorting + intervals | Medium | O(n log n) |
| 4 | `E4_CategoryTree` | Trees, recursion → iteration | Medium | O(n), no stack overflow |
| 5 | `E5_CityTransfers` | Graphs, BFS | Medium | O(V + E) |
| 6 | `E6_BudgetPair` | HashMap ("two sum") | Easy | O(n) |
| 7 | `E7_EventRegistration` | HashMaps + sets + sorting, **GetYourGuide's HackerRank take-home style** | Medium | O(1) register/cancel |

Every method throws `UnsupportedOperationException` until you implement it, so all tests start **red**.

```bash
./mvnw -pl level-1-algorithms test                              # all of level 1
./mvnw -pl level-1-algorithms test -Dtest=E5_CityTransfersTest  # one exercise
```

## How to practise (the interview loop)
1. Read the Javadoc **and the test**. Say the edge cases out loud *before* coding.
2. Say your plan and its complexity. Mention the brute-force option and why you're not starting with it (or why you are).
3. Write the simple version first, run the tests, then optimise.
4. Time-box: about 10 min for Easy, about 15 for Medium.

Reference solutions: `solutions/level-1-algorithms/`. Look only after you've gone green, or after the time-box is up.
