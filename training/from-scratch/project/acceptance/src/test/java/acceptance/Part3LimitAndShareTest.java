package acceptance;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static acceptance.Api.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Part 3: limit and sharing")
class Part3LimitAndShareTest {

    @BeforeAll
    static void appIsUp() {
        checkAppIsRunning();
    }

    @Test
    @DisplayName("The 21st item is rejected with 422")
    void limitOfTwenty() {
        String t = newTraveller();
        for (int i = 1; i <= 20; i++) {
            assertThat(add(t, i, "Tour " + i, "Rome", "10.00").status()).as("item " + i).isEqualTo(201);
        }
        Response r = add(t, 21, "One too many", "Rome", "10.00");
        assertThat(r.status()).as(r.toString()).isEqualTo(422);
        assertThat(get("/travellers/" + t + "/wishlist").json()).hasSize(20);
    }

    @Test
    @DisplayName("Share: 201 with shareId + url; the shared view is live")
    void shareIsALiveView() {
        String t = newTraveller();
        add(t, 1, "Colosseum", "Rome", "49.90");

        Response share = postEmpty("/travellers/" + t + "/wishlist/share");
        assertThat(share.status()).as(share.toString()).isEqualTo(201);
        String shareId = share.json().get("shareId").asText();
        assertThat(share.json().get("url").asText()).isEqualTo("/shared/" + shareId);

        assertThat(get("/shared/" + shareId).json().findValuesAsText("title")).containsExactly("Colosseum");

        add(t, 2, "Vatican", "Rome", "69.00");
        assertThat(get("/shared/" + shareId).json().findValuesAsText("title")).containsExactly("Colosseum", "Vatican");
    }

    @Test
    @DisplayName("Share ids are not guessable")
    void shareIdsAreNotGuessable() {
        String a = postEmpty("/travellers/" + newTraveller() + "/wishlist/share").json().get("shareId").asText();
        String b = postEmpty("/travellers/" + newTraveller() + "/wishlist/share").json().get("shareId").asText();

        assertThat(a).isNotEqualTo(b);
        assertThat(a.length()).as("long enough not to be brute-forced: " + a).isGreaterThanOrEqualTo(16);
        assertThat(a).as("not a sequential number").doesNotMatch("\\d+");
    }

    @Test
    @DisplayName("Unknown share is 404")
    void unknownShare() {
        assertThat(get("/shared/does-not-exist-123456").status()).isEqualTo(404);
    }
}
