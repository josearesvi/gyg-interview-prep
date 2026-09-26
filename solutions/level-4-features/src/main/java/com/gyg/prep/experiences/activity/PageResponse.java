package com.gyg.prep.experiences.activity;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/** FEATURE 1: our own stable JSON shape, rather than serialising Spring's PageImpl, whose shape is not a contract. */
public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {

    public static <E, T> PageResponse<T> of(Page<E> page, Function<E, T> mapper) {
        return new PageResponse<>(page.getContent().stream().map(mapper).toList(), page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }
}
