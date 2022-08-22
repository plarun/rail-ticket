package reference.sample.principal;

import java.io.Serializable;
import java.security.Principal;

public class SamplePrincipal implements Principal, Serializable {
    private final String name;

    public SamplePrincipal(String name) {
        if (name == null)
            throw new NullPointerException("illegal null input");
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public String toString() {
        return "Principal: " + name;
    }

    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (this == o)
            return true;
        if (!(o instanceof SamplePrincipal that))
            return false;

        return this.getName().equals(that.getName());
    }

    public int hashcode() {
        return name.hashCode();
    }
}
