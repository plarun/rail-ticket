package reference.sample;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

class SampleCallBackHandler implements CallbackHandler {
    private final String username;
    private final String password;

    public SampleCallBackHandler(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            if (callback instanceof NameCallback nameCb) {
                nameCb.setName(username);
            } else if (callback instanceof PasswordCallback passwordCb) {
                passwordCb.setPassword(password.toCharArray());
            } else {
                throw new UnsupportedCallbackException(callback);
            }
        }
    }
}

public class AuthTest {
    public static void main(String[] args) {
        LoginContext lc = null;
        System.setProperty("java.security.auth.login.config", "C:\\Users\\arunp\\IdeaProjects\\RailTicket\\src\\test\\java\\jaas.conf");

        final String username = "arunpandi", password = "reference/password";
        try {
            lc = new LoginContext("AuthTestJaas", new SampleCallBackHandler(username, password));
            lc.login();
            System.out.println("Logged in");
            Subject subj = lc.getSubject();
            System.out.println(subj.toString());
            lc.logout();
            System.out.println("Logged out");
        } catch (LoginException | SecurityException e) {
            System.err.println("Cannot create LoginContext. " + e.getMessage());
            System.exit(-1);
        }
    }
}
