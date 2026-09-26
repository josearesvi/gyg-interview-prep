package com.gyg.prep.reviews;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/** A plain unit test, with no Spring context, which the refactoring made possible. Milliseconds, not seconds. */
class ContentModerationTest {

    private final ContentModeration moderation = new ContentModeration();

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "Total SCAM            | true",
            "this is fraudulent    | true",
            "great tour            | false",
            "''                    | false",
    })
    void flagsBlockedWords(String comment, boolean expected) {
        assertThat(moderation.shouldFlag(comment)).isEqualTo(expected);
    }
}
