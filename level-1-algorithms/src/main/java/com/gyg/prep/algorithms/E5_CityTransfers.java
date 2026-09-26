package com.gyg.prep.algorithms;

import java.util.List;
import java.util.Map;

/**
 * Exercise 5 — Graphs / BFS (Medium).
 *
 * {@code routes} lists direct, bidirectional transfers between cities (e.g. "Berlin" <-> "Prague").
 * Return the minimum number of transfers needed to get from {@code from} to {@code to}.
 *
 * Rules:
 *   - from == to returns 0.
 *   - Unreachable, or an unknown city, returns -1.
 *   - Routes are undirected: build the adjacency list in both directions.
 *
 * Talk-aloud prompts: why BFS and not DFS? What changes if every transfer had a price (Dijkstra)?
 */
public final class E5_CityTransfers {

    public int minTransfers(Map<String, List<String>> routes, String from, String to) {
        throw new UnsupportedOperationException("TODO: implement E5");
    }
}
