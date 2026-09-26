# Interviewer, request 3 (last 10–15 minutes: refactor and discuss)

> "Two escalations from Roma Tours:
> 1. *'23 people showed up for a 20-seat departure last Saturday.'* We had a traffic spike that morning.
> 2. *'Your /tours page for Rome takes seconds to load.'* They have ~300 tours."
>
> What's going on? Fix what you can, and talk us through the rest."

What a strong answer covers:
- Why `reserve()` can overbook under concurrency (read-modify-write), and 2–3 fixes with trade-offs.
- A test that proves the fix under concurrent requests.
- Why `GET /tours` is slow (look at the queries per tour), and how to fix it.
- Anything else you noticed during `/onboard`, prioritised.
