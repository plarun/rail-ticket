package reference.password;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class PasswordAuthentication {
    public static final String ID = "$31$";
    public static final int DEFAULT_COST = 16;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int SIZE = 128;
    private static final Pattern layout = Pattern.compile("\\$31\\$(\\d\\d?)\\$(.{43})");
    private final int cost;

    public PasswordAuthentication() {
        this(DEFAULT_COST);
    }

    public PasswordAuthentication(int cost) {
        iterations(cost); /* Validate cost */
        this.cost = cost;
    }

    private static int iterations(int cost) {
        if ((cost < 0) || (cost > 30))
            throw new IllegalArgumentException("cost: " + cost);
        return 1 << cost;
    }

    public String hash(char[] password) {
        // salt
        byte[] salt = new byte[SIZE / 8];
        new SecureRandom().nextBytes(salt);

        // hash
        byte[] dk = pbkdf2(password, salt, 1 << cost);
        byte[] hash = new byte[salt.length + dk.length];
        System.arraycopy(salt, 0, hash, 0, salt.length);
        System.arraycopy(dk, 0, hash, salt.length, dk.length);

        // enc hash
        String encHash = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        return ID + cost + '$' + encHash;
    }

    public boolean authenticate(char[] password, String token) {
        Matcher m = layout.matcher(token);
        if (!m.matches())
            throw new IllegalArgumentException("Invalid token format");

        int iterations = iterations(Integer.parseInt(m.group(1)));

        // dec hash
        byte[] hash = Base64.getUrlDecoder().decode(m.group(2));

        // salt
        byte[] salt = Arrays.copyOfRange(hash, 0, SIZE / 8);

        // dk from given password
        byte[] check = pbkdf2(password, salt, iterations);

        int zero = 0;
        for (int i = 0; i < check.length; ++i)
            zero |= hash[salt.length + i] ^ check[i];
        return zero == 0;
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations) {
        KeySpec spec = new PBEKeySpec(password, salt, iterations, SIZE);
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
            return f.generateSecret(spec).getEncoded();
        }
        catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Missing algorithm: " + ALGORITHM, ex);
        }
        catch (InvalidKeySpecException ex) {
            throw new IllegalStateException("Invalid SecretKeyFactory", ex);
        }
    }
}