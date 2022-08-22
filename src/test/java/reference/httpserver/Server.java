package reference.httpserver;

import com.sun.net.httpserver.HttpServer;
import io.jsonwebtoken.*;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Server {
    private static Server server;

    private static final byte[] secret = Base64.getDecoder().decode("yjIw4GE9Uyb06DQNYLKvG8Nkp6je/mEdhxttJmyD0Io=");

    private static int port;
    private static int backlog;
    private static HttpServer httpServer;

    private final Map<String, String> tokens = new HashMap<>();
    private final Map<String, String> credentials = new HashMap<>();

    private Server() {}

    public synchronized static Server newServer(int port, int backlog) throws IOException {
        if (server == null) {
            server = new Server();
            setPort(port);
            setBacklog(backlog);
            httpServer = HttpServer.create(new InetSocketAddress(port), backlog);
        }
        return server;
    }

    public HttpServer getServer() {
        return httpServer;
    }

    public static void setPort(int port) {
        Server.port = port;
    }

    public static void setBacklog(int backlog) {
        Server.backlog = backlog;
    }

    public void addToken(String key, String token) {
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token);
        tokens.put(key, jws.getSignature());
    }

    public boolean verifyToken(String key, String token) throws LoginException {
        if (tokens.get(key) == null)
            return false;

        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token);
        //{iss=Test server, sub=Test subject, aud=plarun, iat=1658111289, exp=1658111349}
        if (Instant.now().isAfter(jws.getBody().getExpiration().toInstant())) {
            System.out.println("Expired");
            throw new LoginException("JWT token expired");
        }
        return tokens.get(key).equals(jws.getSignature());
    }

    public String generateToken(String username, String password) throws LoginException {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        if (authenticate(username, password)) {
            String token = Jwts.builder()
                    .setIssuer("Test server")
                    .setSubject("Test subject")
                    .setAudience(username)
                    .setIssuedAt(Date.from(Instant.now()))
                    .setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.MINUTES)))
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();
            addToken(username, token);
            return token;
        }

        throw new LoginException("Invalid password");
    }

    public void addUser(String username, String password) throws LoginException {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        if (credentials.get(username) != null)
            throw new LoginException("username already exist");
        credentials.put(username, password);
    }

    private boolean authenticate(String username, String password) throws LoginException {
        if (credentials.get(username) == null)
            throw new LoginException("user doesn't exist");
        return credentials.get(username).equals(password);
    }

    @Override
    public String toString() {
        return "Server running at port " + port + " with backlog " + backlog;
    }
}
