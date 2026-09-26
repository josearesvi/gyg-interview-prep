package com.gyg.prep.algorithms;

import java.util.ArrayList;
import java.util.List;

/** A node of the category tree, e.g. Tours -> Walking tours -> Food tours. */
public final class Category {
    private final String name;
    private final int directActivityCount;
    private final List<Category> children = new ArrayList<>();

    public Category(String name, int directActivityCount) {
        this.name = name;
        this.directActivityCount = directActivityCount;
    }

    public Category add(Category child) {
        children.add(child);
        return this;
    }

    public String name() { return name; }
    public int directActivityCount() { return directActivityCount; }
    public List<Category> children() { return children; }
}
