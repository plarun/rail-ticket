package rail.api.model.user;

public record AppUser(String username, String password, String email, Integer age, String gender) {

    String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }
}
