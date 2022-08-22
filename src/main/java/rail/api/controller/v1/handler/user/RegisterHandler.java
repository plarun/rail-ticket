package rail.api.controller.v1.handler.user;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import rail.api.controller.v1.request.RegisterUserForm;
import rail.api.exception.DuplicateUserException;
import rail.api.service.UserAction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class RegisterHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream inStream = exchange.getRequestBody();
        String reqBody = new String(inStream.readAllBytes(), StandardCharsets.UTF_8);

        String msg = "user created";
        int statusCode = 200;

        try {
            RegisterUserForm userForm = new RegisterUserForm(reqBody);
            UserAction action = new UserAction();
            action.createUser(userForm);
        } catch (SQLException | ClassNotFoundException e) {
            msg = e.getMessage();
            statusCode = 500;
        } catch (JSONException e) {
            msg = e.getMessage();
            statusCode = 400;
        } catch(DuplicateUserException e) {
            msg = e.getMessage();
            statusCode = 409;
        }

        exchange.sendResponseHeaders(statusCode, msg.length());
        OutputStream outStream = exchange.getResponseBody();
        outStream.write(msg.getBytes());
    }
}
