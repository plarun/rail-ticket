package reference.httpserver;

import com.sun.net.httpserver.HttpServer;
import org.json.JSONException;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = Server.newServer(8060, 0);
        HttpServer httpServer = server.getServer();

        // POST
        // /register
        httpServer.createContext("/register", exchange -> {
            System.out.println(exchange.getRequestMethod() + " " + exchange.getRequestURI().toString());

            InputStream is = exchange.getRequestBody();
            String reqBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            String username, password, msg = "User created";
            int statusCode = 200;

            try {
                JSONObject json = new JSONObject(reqBody);
                username = (String) json.get("username");
                password = (String) json.get("reference/password");

                server.addUser(username, password);
            } catch (LoginException e) {
                msg = e.getMessage();
                statusCode = 401;
            } catch (JSONException je) {
                msg = je.getMessage();
                statusCode = 400;
            }

            OutputStream os = exchange.getResponseBody();
            exchange.sendResponseHeaders(statusCode, msg.length());
            os.write(msg.getBytes());
        });

        // POST
        // /login
        httpServer.createContext("/login", exchange -> {
            System.out.println(exchange.getRequestMethod() + " " + exchange.getRequestURI().toString());

            InputStream is = exchange.getRequestBody();
            String reqBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            String username, password;
            String token;
            int statusCode = 200;

            try {
                JSONObject json = new JSONObject(reqBody);
                username = (String) json.get("username");
                password = (String) json.get("reference/password");

                token = server.generateToken(username, password);
            } catch (JSONException | LoginException e) {
                System.out.println(e.getMessage());
                token = e.getMessage();
                statusCode = 401;
            }

            System.out.println("msg: " + token + ", code: " + statusCode);

            OutputStream os = exchange.getResponseBody();
            exchange.sendResponseHeaders(statusCode, token.length());
            os.write(token.getBytes());
        });

        // GET
        // http://localhost:8010/test
        httpServer.createContext("/resource", exchange -> {
            System.out.println(exchange.getRequestMethod() + " " + exchange.getRequestURI().toString());

            String username = exchange.getRequestURI().getQuery().split("=")[1];
            List<String> auth = exchange.getRequestHeaders().get("Authorization");

            System.out.println("username: " + username);
            System.out.println("auth: " + auth);
            String token = null;
            if (auth != null)
                token = auth.get(0).split("\\s")[1];

            int statusCode = 200;
            String msg = "access granted";

            if (token == null) {
                statusCode = 401;
                msg = "unauthorized access";
            }

            try {
                if (!server.verifyToken(username, token)) {
                    statusCode = 400;
                    msg = "bad request";
                }
            } catch (LoginException e) {
                statusCode = 400;
                msg = e.getMessage();
            }

            exchange.sendResponseHeaders(statusCode, msg.length());
            OutputStream os = exchange.getResponseBody();
            os.write(msg.getBytes());
            os.close();
        });

        httpServer.createContext("/test", exchange -> {
            String msg = "test post response";
            exchange.sendResponseHeaders(200, msg.length());
            OutputStream os = exchange.getResponseBody();
            os.write(msg.getBytes());
            os.close();
        });

        httpServer.setExecutor(null);
        httpServer.start();
        System.out.println("server running at " + httpServer.getAddress().toString());
    }
}