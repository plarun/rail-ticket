package rail.api.controller.v1.request;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateUserForm {
    private final String userName;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Integer age;
    private final String gender;

    public UpdateUserForm(String reqBody) throws JSONException {
        JSONObject json = new JSONObject(reqBody);
        userName = json.has("username") ? json.getString("username") : null;
        firstName = json.has("firstname") ? json.getString("firstname") : null;
        lastName = json.has("lastname") ? json.getString("lastname") : null;
        email = json.has("email") ? json.getString("email") : null;
        age = json.has("age") ? json.getInt("age") : null;
        gender = json.has("gender") ? json.getString("gender") : null;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }
}
