package reference.httpclient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Client {
    private final HttpClient client;
    private final int port;

    public Client(int port) {
        this.port = port;
        client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(10))
                .proxy(ProxySelector.of(new InetSocketAddress("localhost", port)))
                .build();
    }

    private String getURI(String path) {
        return "http://localhost:" + port + path;
    }

    public void createUser(String username, String password) throws IOException, InterruptedException {
        System.out.println("register client");
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(getURI("/register")))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"username\":\"" +
                                username +
                                "\",\"password\":\"" +
                                password + "\"}"
                ))
                .build();

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println("status: " + res.statusCode());
        System.out.println("body: " + res.body());
    }

    public void loginUser(String username, String password) throws IOException, InterruptedException {
        System.out.println("login client");
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(getURI("/login")))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"username\":\"" +
                                username +
                                "\",\"password\":\"" +
                                password + "\"}"
                ))
                .build();

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println("status: " + res.statusCode());
        System.out.println("body: " + res.body());
    }

    public void accessResource(String token) throws IOException, InterruptedException {
        System.out.println("access client");
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(getURI("/resource")))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .setHeader("Authentication", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println("status: " + res.statusCode());
        System.out.println("body: " + res.body());
    }
}
