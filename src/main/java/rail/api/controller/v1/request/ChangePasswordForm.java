package rail.api.controller.v1.request;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordForm {
    private final String username;
    private final String oldPassword;
    private final String newPassword;
    private final String confirmPassword;

    public ChangePasswordForm(String reqBody) throws JSONException {
        JSONObject json = new JSONObject(reqBody);
        username = json.has("username") ? json.getString("username") : null;
        oldPassword = json.has("oldPassword") ? json.getString("oldPassword") : null;
        newPassword = json.has("newPassword") ? json.getString("newPassword") : null;
        confirmPassword = json.has("confirmPassword") ? json.getString("confirmPassword") : null;
    }

    public String getUsername() {
        return username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
