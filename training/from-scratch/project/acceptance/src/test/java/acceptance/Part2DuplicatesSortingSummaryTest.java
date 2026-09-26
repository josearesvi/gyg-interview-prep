package acceptance;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static acceptance.Api.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Part 2: duplicates, sorting, summary")
class Part2DuplicatesSortingSummaryTest {

    @BeforeAll
    static void appIsUp() {
        checkAppIsRunning();
    }

    @Test
    @DisplayName("Adding the same activity twice is 409; another traveller can still add it")
    void noDuplicates() {
        String t = newTraveller();
        assertThat(add(t, 42, "Colosseum", "Rome", "49.90").status()).isEqualTo(201);
        Response dup = add(t, 42, "Colosseum", "Rome", "49.90");
        assertThat(dup.status()).as(dup.toString()).isEqualTo(409);
        assertThat(get("/travellers/" + t + "/wishlist").json()).hasSize(1);

        assertThat(add(newTraveller(), 42, "Colosseum", "Rome", "49.90").status()).isEqualTo(201);
    }

    @Test
    @DisplayName("sort=price_asc / price_desc; default keeps insertion order")
    void sorting() {
        String t = newTraveller(), path = "/travellers/" + t + "/wishlist";
        add(t, 1, "Mid", "Rome", "20.00");
        add(t, 2, "Cheap", "Rome", "5.50");
        add(t, 3, "Pricey", "Rome", "99.00");

        assertThat(get(path).json().findValuesAsText("title")).containsExactly("Mid", "Cheap", "Pricey");
        assertThat(get(path + "?sort=price_asc").json().findValuesAsText("title"))
                .containsExactly("Cheap", "Mid", "Pricey");
        assertThat(get(path + "?sort=price_desc").json().findValuesAsText("title"))
                .containsExactly("Pricey", "Mid", "Cheap");
    }

    @Test
    @DisplayName("Unknown sort value is 400")
    void unknownSort() {
        Response r = get("/travellers/" + newTraveller() + "/wishlist?sort=popularity");
        assertThat(r.status()).as(r.toString()).isEqualTo(400);
    }

    @Test
    @DisplayName("Summary: count and exact total")
    void summary() {
        String t = newTraveller();
        add(t, 1, "A", "Rome", "0.10");
        add(t, 2, "B", "Rome", "0.20");

        Response r = get("/travellers/" + t + "/wishlist/summary");
        assertThat(r.status()).as(r.toString()).isEqualTo(200);
        assertThat(r.json().get("count").asInt()).isEqualTo(2);
        assertThat(r.decimal("totalPrice")).as("exact to the cent: " + r).isEqualByComparingTo("0.30");
        assertThat(r.json().get("currency").asText()).isEqualTo("EUR");
    }

    @Test
    @DisplayName("Summary of an empty wishlist")
    void emptySummary() {
        Response r = get("/travellers/" + newTraveller() + "/wishlist/summary");
        assertThat(r.status()).isEqualTo(200);
        assertThat(r.json().get("count").asInt()).isZero();
        assertThat(r.decimal("totalPrice")).isEqualByComparingTo("0");
    }
}
