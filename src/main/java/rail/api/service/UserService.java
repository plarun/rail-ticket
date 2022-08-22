package rail.api.service;

import rail.api.controller.v1.request.ChangePasswordForm;
import rail.api.controller.v1.request.LoginUserForm;
import rail.api.controller.v1.request.RegisterUserForm;
import rail.api.controller.v1.request.UpdateUserForm;
import rail.api.exception.DuplicateUserException;
import rail.api.exception.UserNotExistException;
import rail.api.exception.WrongPasswordException;
import rail.api.model.user.AppUser;

import java.sql.SQLException;

public interface UserService {
    void createUser(RegisterUserForm userForm) throws SQLException, DuplicateUserException;

    String authenticateUser(LoginUserForm userForm) throws SQLException, UserNotExistException, WrongPasswordException;

    AppUser findUser(String username) throws SQLException;

    boolean updateProfile(UpdateUserForm userForm) throws SQLException;

    boolean changePassword(ChangePasswordForm userForm) throws Exception;
}
