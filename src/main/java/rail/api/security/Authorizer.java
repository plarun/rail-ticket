package rail.api.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

public class Authorizer {
    private static Authorizer authorizer;
    private static Key secretKey;
    private static final int DEFAULT_EXPIRE_TIME_IN_MINUTE = 60;
    private static final String ISSUER = "rail.example.com";

    private Authorizer() {}

    public static Authorizer getAuthorizer() {
        return authorizer;
    }

    public synchronized static void init() {
        if (authorizer == null) {
            try {
                InputStream in = new FileInputStream("C:/Users/arunp/IdeaProjects/RailTicket/src/main/resources/config.properties");

                Properties prop = new Properties();
                prop.load(in);

                authorizer = new Authorizer();
                authorizer.setSecretKey(prop.getProperty("jwt.secretkey"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSecretKey(String secretKey) {
        Authorizer.secretKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken() {
        Instant now = Instant.now();
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(DEFAULT_EXPIRE_TIME_IN_MINUTE, ChronoUnit.MINUTES)))
                .setId(String.valueOf(UUID.randomUUID()))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) throws JSONException, NoSuchAlgorithmException, InvalidKeyException {
        String[] tokenParts = token.split("\\s");

        if (tokenParts.length != 3)
            throw new IllegalArgumentException("Invalid token format");

        final JSONObject header = new JSONObject(Base64.getDecoder().decode(tokenParts[0]));
        final JSONObject payload = new JSONObject(Base64.getDecoder().decode(tokenParts[1]));
        final String signature = new String(Base64.getDecoder().decode(tokenParts[2]), StandardCharsets.UTF_8);
        final String headerAndPayload = header + "." + payload;

        return isTokenExpired(payload.getLong("exp")) && isTokenValid(signature, headerAndPayload);
    }

    private boolean isTokenExpired(long exp) {
        return Instant.now().isAfter(Instant.ofEpochSecond(exp));
    }

    private boolean isTokenValid(String signature, String headerAndPayload) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hs256 = Mac.getInstance(SignatureAlgorithm.HS256.getJcaName());
        hs256.init(secretKey);

        String generatedSignature = Base64.getEncoder()
                .encodeToString(hs256.doFinal(headerAndPayload.getBytes(StandardCharsets.UTF_8)));
        return signature.equals(generatedSignature);
    }
}
