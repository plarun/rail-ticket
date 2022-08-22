package rail.api.exception;

public class DuplicateUserException extends Exception {
    public DuplicateUserException() {
        super("user already exist");
    }

    public DuplicateUserException(String msg) {
        super(msg);
    }
}
