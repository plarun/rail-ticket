package rail.api.controller.v1.request;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterUserForm {
    private final String username;
    private final String password;
    private final String email;

    public RegisterUserForm(String reqBody) throws JSONException {
        JSONObject json = new JSONObject(reqBody);
        username = json.has("username") ? json.getString("username") : null;
        password = json.has("password") ? json.getString("password") : null;
        email = json.has("email") ? json.getString("email") : null;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
