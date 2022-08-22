package rail.api.controller.v1.handler.user;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ProfileHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream inStream = exchange.getRequestBody();
        String reqBody = new String(inStream.readAllBytes(), StandardCharsets.UTF_8);

        String msg = "user created";
        int statusCode = 200;


    }
}
