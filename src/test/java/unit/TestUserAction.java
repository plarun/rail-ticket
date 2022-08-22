package unit;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TestUserAction {
    static final int PORT = 8060;
    final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(10))
            .proxy(ProxySelector.of(new InetSocketAddress("localhost", PORT)))
            .build();

    static String getURI(String path) {
        return "http://localhost:" + PORT + path;
    }

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("Test user account creation")
    @Order(1)
    void userSignup() throws IOException, InterruptedException, JSONException {
        String username = "plarun";
        String password = "plarun";

        String reqBody = new JSONObject()
                .put("username", username)
                .put("password", password)
                .toString();

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(getURI("/api/v1/register")))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(reqBody))
                .build();

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, res.statusCode());
    }

    @Test
    @DisplayName("Test duplicate user account creation")
    @Order(2)
    void duplicateUserSignup() throws JSONException, IOException, InterruptedException {
        String username = "plarun";
        String password = "plarun2";

        String reqBody = new JSONObject()
                .put("username", username)
                .put("password", password)
                .toString();

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(getURI("/api/v1/register")))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(reqBody))
                .build();

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, res.statusCode());
    }

    @Test
    @Disabled
    void userLogin() {

    }
}
