package acceptance;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

/** A tiny HTTP client for the tests. It knows nothing about how your app is built. */
final class Api {

    static final String BASE_URL = System.getProperty("BASE_URL",
            System.getenv().getOrDefault("BASE_URL", "http://localhost:8080"));

    private static final HttpClient HTTP = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();
    private static final ObjectMapper JSON = new ObjectMapper();

    record Response(int status, String raw) {
        JsonNode json() {
            try {
                return raw == null || raw.isBlank() ? JSON.nullNode() : JSON.readTree(raw);
            } catch (IOException e) {
                throw new AssertionError("Response is not JSON: " + raw, e);
            }
        }

        BigDecimal decimal(String field) {
            return new BigDecimal(json().get(field).asText());
        }

        @Override
        public String toString() {
            return "HTTP " + status + " " + raw;
        }
    }

    static String newTraveller() {
        return "t-" + UUID.randomUUID();
    }

    static String item(long activityId, String title, String city, String price) {
        return """
                {"activityId": %d, "title": "%s", "city": "%s", "price": %s}""".formatted(activityId, title, city, price);
    }

    static Response get(String path) {
        return send(HttpRequest.newBuilder(uri(path)).GET());
    }

    static Response post(String path, String json) {
        return send(HttpRequest.newBuilder(uri(path)).header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json)));
    }

    static Response postEmpty(String path) {
        return send(HttpRequest.newBuilder(uri(path)).POST(HttpRequest.BodyPublishers.noBody()));
    }

    static Response delete(String path) {
        return send(HttpRequest.newBuilder(uri(path)).DELETE());
    }

    static Response add(String traveller, long activityId, String title, String city, String price) {
        return post("/travellers/" + traveller + "/wishlist", item(activityId, title, city, price));
    }

    /** Fails fast, with a helpful message, when the app isn't running. */
    static void checkAppIsRunning() {
        try {
            HTTP.send(HttpRequest.newBuilder(uri("/")).GET().build(), HttpResponse.BodyHandlers.discarding());
        } catch (ConnectException e) {
            fail("Nothing is listening on " + BASE_URL + ". Start your app first (IntelliJ: Run > 'App: wishlist-api', "
                    + "or Ctrl+Shift+R on your *Application class), or pass -DBASE_URL=http://localhost:<port>.");
        } catch (IOException | InterruptedException e) {
            fail("Could not reach " + BASE_URL + ": " + e);
        }
    }

    private static URI uri(String path) {
        return URI.create(BASE_URL + path);
    }

    private static Response send(HttpRequest.Builder request) {
        try {
            HttpResponse<String> r = HTTP.send(request.timeout(Duration.ofSeconds(10)).build(),
                    HttpResponse.BodyHandlers.ofString());
            return new Response(r.statusCode(), r.body());
        } catch (IOException | InterruptedException e) {
            throw new AssertionError("Request failed against " + BASE_URL + ": " + e, e);
        }
    }

    private Api() {
    }
}
