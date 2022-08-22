package rail.api.exception;

public class WrongPasswordException extends Exception {
    public WrongPasswordException() {
        super("wrong password");
    }

    public WrongPasswordException(String msg) {
        super(msg);
    }
}
