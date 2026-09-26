package com.gyg.prep.algorithms;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reference solution. Both parts are iterative, so a 50k-deep tree cannot overflow the call stack.
 *
 * The recursive version of (a), which is fine to write FIRST in an interview and then improve:
 *   int total(Category c) { return c.directActivityCount() + c.children().stream().mapToInt(this::total).sum(); }
 */
public final class E4_CategoryTree {

    public int totalActivities(Category root) {
        if (root == null) return 0;
        int total = 0;
        Deque<Category> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Category c = stack.pop();
            total += c.directActivityCount();
            c.children().forEach(stack::push);
        }
        return total;
    }

    public List<String> pathTo(Category root, String name) {
        if (root == null) return List.of();
        // DFS that remembers each node's parent, then walks back up from the match.
        Map<Category, Category> parent = new HashMap<>();
        Deque<Category> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Category c = stack.pop();
            if (c.name().equals(name)) {
                List<String> path = new ArrayList<>();
                for (Category n = c; n != null; n = parent.get(n)) path.add(n.name());
                Collections.reverse(path);
                return path;
            }
            for (Category child : c.children()) {
                parent.put(child, c);
                stack.push(child);
            }
        }
        return List.of();
    }
}
