package rail.api.exception;

public class UserNotExistException extends Exception {
    public UserNotExistException() {
        super("user not exist");
    }

    public UserNotExistException(String msg) {
        super(msg);
    }
}
