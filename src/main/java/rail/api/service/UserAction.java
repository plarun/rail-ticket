package rail.api.service;

import rail.api.controller.v1.request.ChangePasswordForm;
import rail.api.controller.v1.request.LoginUserForm;
import rail.api.controller.v1.request.RegisterUserForm;
import rail.api.controller.v1.request.UpdateUserForm;
import rail.api.exception.DuplicateUserException;
import rail.api.exception.UserNotExistException;
import rail.api.exception.WrongPasswordException;
import rail.api.model.user.AppUser;
import rail.api.security.Authorizer;
import rail.api.security.SecurePassword;
import rail.api.server.DbServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UserAction implements UserService {
    private final Connection conn;

    public UserAction() throws SQLException, ClassNotFoundException {
        conn = DbServer.getConnection();
    }

    @Override
    public void createUser(RegisterUserForm userForm) throws SQLException, DuplicateUserException {
        AppUser user = findUser(userForm.getUsername());
        if (user != null)
            throw new DuplicateUserException();

        // secure the password with salt and hash
        SecurePassword securePassword = new SecurePassword();
        String hashedPassword = securePassword.hash(userForm.getPassword().toCharArray());

        String query = DbServer.getQuery("query.insert.registerUser");
        PreparedStatement prepStmt = conn.prepareStatement(query);
        prepStmt.setString(1, userForm.getUsername());
        prepStmt.setString(2, hashedPassword);
        prepStmt.setString(3, userForm.getEmail());
        prepStmt.execute();
    }

    @Override
    public String authenticateUser(LoginUserForm userForm) throws SQLException, UserNotExistException, WrongPasswordException {
        AppUser user = findUser(userForm.getUsername());
        if (user == null)
            throw new UserNotExistException();

        SecurePassword securePassword = new SecurePassword();
        boolean isMatched = securePassword.authenticate(userForm.getPassword().toCharArray(), user.getPassword());

        if (!isMatched)
            throw new WrongPasswordException();

        return Authorizer.getAuthorizer().generateToken();
    }

    @Override
    public AppUser findUser(String username) throws SQLException {
        String query = DbServer.getQuery("query.select.findUser");
        PreparedStatement prepStmt = conn.prepareStatement(query);
        prepStmt.setString(1, username);
        ResultSet result = prepStmt.executeQuery();

        if (!result.next())
            return null;
        return new AppUser(
                result.getString(1),
                result.getString(2),
                result.getString(3),
                result.getInt(4),
                result.getString(5)
        );
    }

    @Override
    public boolean updateProfile(UpdateUserForm userForm) throws SQLException {
        String query = "Update ussr Set";
        if (userForm.getGender() != null)
            query += " gender = " + userForm.getGender();
        if (userForm.getAge() != null)
            query += " age = " + userForm.getAge();

        query += " Where user_name = ?";

        PreparedStatement prepStmt = conn.prepareStatement(query);
        prepStmt.setString(1, userForm.getUserName());
        return prepStmt.execute();
    }

    @Override
    public boolean changePassword(ChangePasswordForm userForm) throws Exception {
        Objects.requireNonNull(userForm.getOldPassword(), "required old password");
        Objects.requireNonNull(userForm.getNewPassword(), "required new password");
        Objects.requireNonNull(userForm.getConfirmPassword(), "required confirm password");

        AppUser user = findUser(userForm.getUsername());
        Objects.requireNonNull(user, "user doesn't exist");

        if (! userForm.getOldPassword().equals(user.getPassword()))
            throw new Exception("password mismatch");

        if (! userForm.getNewPassword().equals(userForm.getConfirmPassword()))
            throw new Exception("new password and confirm password not same");

        String query = DbServer.getQuery("query.update.changePassword");
        PreparedStatement prepStmt = conn.prepareStatement(query);
        prepStmt.setString(1, userForm.getNewPassword());
        prepStmt.setString(2, userForm.getUsername());
        return prepStmt.execute();
    }
}
