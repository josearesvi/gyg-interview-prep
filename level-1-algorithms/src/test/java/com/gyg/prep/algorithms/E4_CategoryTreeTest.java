package com.gyg.prep.algorithms;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class E4_CategoryTreeTest {

    private final E4_CategoryTree sut = new E4_CategoryTree();

    private static Category tree() {
        Category food = new Category("Food tours", 7);
        Category walking = new Category("Walking tours", 3).add(food).add(new Category("Night walks", 2));
        Category boat = new Category("Boat tours", 5);
        return new Category("Tours", 1).add(walking).add(boat);
    }

    @Test
    void totalIncludesAllDescendants() {
        assertThat(sut.totalActivities(tree())).isEqualTo(1 + 3 + 7 + 2 + 5);
    }

    @Test
    void leafTotalIsItsOwnCount() {
        assertThat(sut.totalActivities(new Category("Leaf", 4))).isEqualTo(4);
    }

    @Test
    void pathToNestedCategory() {
        assertThat(sut.pathTo(tree(), "Food tours")).containsExactly("Tours", "Walking tours", "Food tours");
        assertThat(sut.pathTo(tree(), "Boat tours")).containsExactly("Tours", "Boat tours");
        assertThat(sut.pathTo(tree(), "Tours")).containsExactly("Tours");
    }

    @Test
    void pathToMissingCategoryIsEmpty() {
        assertThat(sut.pathTo(tree(), "Skydiving")).isEmpty();
    }

    @Test
    void deepTreeDoesNotBlowTheStack() {
        Category root = new Category("c0", 1);
        Category cur = root;
        for (int i = 1; i < 50_000; i++) {
            Category next = new Category("c" + i, 1);
            cur.add(next);
            cur = next;
        }
        // Passes with a recursive solution only if you are lucky with the stack size. Make it iterative.
        assertThat(sut.totalActivities(root)).isEqualTo(50_000);
    }
}
