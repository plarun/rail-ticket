package rail.api.controller.v1.handler.user;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import rail.api.controller.v1.request.LoginUserForm;
import rail.api.exception.UserNotExistException;
import rail.api.exception.WrongPasswordException;
import rail.api.service.UserAction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
            InputStream inStream = exchange.getRequestBody();
            String reqBody = new String(inStream.readAllBytes(), StandardCharsets.UTF_8);

            String token;
            int statusCode = 200;

            try {
                LoginUserForm userForm = new LoginUserForm(reqBody);
                UserAction action = new UserAction();
                token = action.authenticateUser(userForm);
            } catch (JSONException e) {
                token = e.getMessage();
                statusCode = 400;
            }  catch (WrongPasswordException e) {
                token = e.getMessage();
                statusCode = 401;
            } catch (UserNotExistException e) {
                token = e.getMessage();
                statusCode = 404;
            } catch (SQLException | ClassNotFoundException e) {
                token = e.getMessage();
                statusCode = 500;
            }

        exchange.sendResponseHeaders(statusCode, token.length());
            OutputStream outStream = exchange.getResponseBody();
            outStream.write(token.getBytes());
    }
}
