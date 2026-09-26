# Training A: start from scratch (about 50 min)

This simulates *"here are the requirements, build it"*. The requirements come from the "interviewer":
they live in the prep repo under `training/from-scratch/sealed/` and are revealed one part at a time. You create the app yourself, **inside this folder**,
with `/scaffold`. The `acceptance/` project is a black-box test suite that plays the interviewer: it calls your API
over HTTP, whatever your internals look like.

```
from-scratch/
├── acceptance/                      ← black-box HTTP tests: Part1…, Part2…, Part3…
├── .run/                            ← IntelliJ run configurations (Run menu, ⌃⌥R)
└── wishlist-api/                    ← YOU create this with /scaffold
```

Step-by-step guide: `docs/WALKTHROUGH_1.md` in the prep repo.

- Start your app (IntelliJ: *Run → "App: wishlist-api"*, or ⌃⇧R on the `…Application` class), then run
  *"Acceptance: Part 1"* from the Run menu.
- Base URL defaults to `http://localhost:8080`. To change it: `-DBASE_URL=http://localhost:9090`.
