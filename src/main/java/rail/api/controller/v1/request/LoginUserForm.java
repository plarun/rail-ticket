package rail.api.controller.v1.request;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginUserForm {
    private final String username;
    private final String password;

    public LoginUserForm(String reqBody) throws JSONException {
        JSONObject json = new JSONObject(reqBody);
        username = json.getString("username");
        password = json.getString("password");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
