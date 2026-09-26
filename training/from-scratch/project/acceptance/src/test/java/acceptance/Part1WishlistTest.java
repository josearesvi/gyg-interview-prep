package acceptance;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static acceptance.Api.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Part 1: add, list, remove")
class Part1WishlistTest {

    @BeforeAll
    static void appIsUp() {
        checkAppIsRunning();
    }

    @Test
    @DisplayName("POST returns 201 with the stored item")
    void addReturnsTheItem() {
        String t = newTraveller();
        Response r = add(t, 123, "Colosseum tour", "Rome", "49.90");

        assertThat(r.status()).as(r.toString()).isEqualTo(201);
        assertThat(r.json().hasNonNull("id")).as("has an id: " + r).isTrue();
        assertThat(r.json().get("activityId").asLong()).isEqualTo(123);
        assertThat(r.json().get("title").asText()).isEqualTo("Colosseum tour");
        assertThat(r.json().get("city").asText()).isEqualTo("Rome");
        assertThat(r.decimal("price")).isEqualByComparingTo("49.90");
        assertThat(r.json().hasNonNull("addedAt")).as("has addedAt: " + r).isTrue();
    }

    @Test
    @DisplayName("GET lists items in the order they were added")
    void listsInInsertionOrder() {
        String t = newTraveller();
        add(t, 1, "B tour", "Rome", "20.00");
        add(t, 2, "A tour", "Rome", "10.00");

        Response r = get("/travellers/" + t + "/wishlist");
        assertThat(r.status()).as(r.toString()).isEqualTo(200);
        assertThat(r.json()).hasSize(2);
        assertThat(r.json().get(0).get("activityId").asLong()).isEqualTo(1);
        assertThat(r.json().get(1).get("activityId").asLong()).isEqualTo(2);
    }

    @Test
    @DisplayName("A traveller with nothing saved gets []")
    void emptyWishlist() {
        Response r = get("/travellers/" + newTraveller() + "/wishlist");
        assertThat(r.status()).isEqualTo(200);
        assertThat(r.json().isArray()).as(r.toString()).isTrue();
        assertThat(r.json()).isEmpty();
    }

    @Test
    @DisplayName("Travellers only see their own items")
    void travellersAreIsolated() {
        String anna = newTraveller(), ben = newTraveller();
        add(anna, 7, "Boat tour", "Lisbon", "15.00");

        assertThat(get("/travellers/" + ben + "/wishlist").json()).isEmpty();
    }

    @Test
    @DisplayName("DELETE returns 204 and removes the item; deleting again is 404")
    void deleteRemoves() {
        String t = newTraveller();
        long id = add(t, 5, "Wine tasting", "Porto", "30.00").json().get("id").asLong();
        add(t, 6, "Fado night", "Porto", "25.00");

        assertThat(delete("/travellers/" + t + "/wishlist/" + id).status()).isEqualTo(204);
        Response list = get("/travellers/" + t + "/wishlist");
        assertThat(list.json()).hasSize(1);
        assertThat(list.json().get(0).get("activityId").asLong()).isEqualTo(6);

        assertThat(delete("/travellers/" + t + "/wishlist/" + id).status()).isEqualTo(404);
    }

    @Test
    @DisplayName("Cannot delete another traveller's item (404)")
    void cannotDeleteSomeoneElsesItem() {
        String owner = newTraveller(), other = newTraveller();
        long id = add(owner, 5, "Wine tasting", "Porto", "30.00").json().get("id").asLong();

        assertThat(delete("/travellers/" + other + "/wishlist/" + id).status()).isEqualTo(404);
        assertThat(get("/travellers/" + owner + "/wishlist").json()).hasSize(1);
    }

    @Test
    @DisplayName("Invalid input is 400")
    void validation() {
        String t = newTraveller(), path = "/travellers/" + t + "/wishlist";
        assertThat(post(path, """
                {"title": "No activity id", "city": "Rome", "price": 1}""").status()).isEqualTo(400);
        assertThat(post(path, """
                {"activityId": 1, "city": "Rome", "price": 1}""").status()).isEqualTo(400);
        assertThat(post(path, """
                {"activityId": 1, "title": "  ", "city": "Rome", "price": 1}""").status()).isEqualTo(400);
        assertThat(post(path, """
                {"activityId": 1, "title": "No city", "price": 1}""").status()).isEqualTo(400);
        assertThat(post(path, """
                {"activityId": 1, "title": "Negative", "city": "Rome", "price": -0.01}""").status()).isEqualTo(400);
        assertThat(get(path).json()).as("nothing invalid was stored").isEmpty();
    }

    @Test
    @DisplayName("Free activities (price 0) are allowed")
    void zeroPriceIsFine() {
        Response r = add(newTraveller(), 9, "Free walking tour", "Berlin", "0");
        assertThat(r.status()).as(r.toString()).isEqualTo(201);
        assertThat(r.decimal("price")).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
