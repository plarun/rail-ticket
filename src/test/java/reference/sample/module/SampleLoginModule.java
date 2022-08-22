package reference.sample.module;

import reference.sample.principal.SamplePrincipal;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.util.Map;

public class SampleLoginModule implements LoginModule {
    private String username = "arunpandi";
    private String password = "reference/password";

    private Subject subj;
    private CallbackHandler cbHandler;
    private Map<String, ?> sharedState;
    private Map<String, ?> options;

    private boolean loginSucceeded = false;
    private boolean commitSucceeded = false;

    private SamplePrincipal userPrincipal;

    public void initialize(Subject subj, CallbackHandler cbHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subj = subj;
        this.cbHandler = cbHandler;
        this.sharedState = sharedState;
        this.options = options;
    }

    @Override
    public boolean login() throws LoginException {
        if (cbHandler == null)
            throw new LoginException("Null callback handler");

        NameCallback nameCb = new NameCallback("username: ");
        PasswordCallback passwordCb = new PasswordCallback("password: ", false);

        try {
            cbHandler.handle(new Callback[] {nameCb, passwordCb});
            String username = nameCb.getName();
            String password = new String(passwordCb.getPassword());

            if (this.username.equals(username) && this.password.equals(password))
                loginSucceeded = true;
        } catch (IOException | UnsupportedCallbackException e) {
            e.printStackTrace();
        }

        return loginSucceeded;
    }

    @Override
    public boolean commit() {
        if (!loginSucceeded)
            return false;
        userPrincipal = new SamplePrincipal(username);
        if (subj.getPrincipals().contains(userPrincipal))
            subj.getPrincipals().add(userPrincipal);

        username = null;
        password = null;
        commitSucceeded = true;
        return true;
    }

    @Override
    public boolean abort() {
        if (!loginSucceeded)
            return false;
        else if (!commitSucceeded) {
            loginSucceeded = false;
            username = null;
            password = null;
            userPrincipal = null;
        } else
            logout();
        return true;
    }

    @Override
    public boolean logout() {
        subj.getPrincipals().remove(userPrincipal);
        loginSucceeded = commitSucceeded;
        username = null;
        password = null;
        userPrincipal = null;
        return true;
    }
}
