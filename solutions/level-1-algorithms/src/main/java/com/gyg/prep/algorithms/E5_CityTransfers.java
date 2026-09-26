package com.gyg.prep.algorithms;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/** Reference solution: BFS on an unweighted, undirected graph. O(V + E). */
public final class E5_CityTransfers {

    public int minTransfers(Map<String, List<String>> routes, String from, String to) {
        if (from.equals(to)) return 0;

        Map<String, Set<String>> graph = new HashMap<>();
        routes.forEach((a, targets) -> targets.forEach(b -> {
            graph.computeIfAbsent(a, x -> new HashSet<>()).add(b);
            graph.computeIfAbsent(b, x -> new HashSet<>()).add(a); // undirected
        }));
        if (!graph.containsKey(from) || !graph.containsKey(to)) return -1;

        Map<String, Integer> dist = new HashMap<>();   // doubles as the "visited" set
        Queue<String> queue = new ArrayDeque<>();
        dist.put(from, 0);
        queue.add(from);
        while (!queue.isEmpty()) {
            String city = queue.poll();
            for (String next : graph.get(city)) {
                if (dist.containsKey(next)) continue;
                int d = dist.get(city) + 1;
                if (next.equals(to)) return d;         // BFS: the first hit is the shortest
                dist.put(next, d);
                queue.add(next);
            }
        }
        return -1;
    }
}
