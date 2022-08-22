package reference.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class JWT {
    private static final byte[] secret = Base64.getDecoder().decode("yjIw4GE9Uyb06DQNYLKvG8Nkp6je/mEdhxttJmyD0Io=");

    public static String buildJWT() {
        Instant now = Instant.now();

        String jwt = Jwts.builder()
                .setIssuer("arun")
                .setSubject("Test JWT")
                .setAudience("plarun,arunp")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1, ChronoUnit.MINUTES)))
                .setId(String.valueOf(UUID.randomUUID()))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return jwt;
    }

    public static Jws<Claims> parseJWT(String jwt) {
        return Jwts.parser()
                .requireAudience("plarun,arunp")
                .setSigningKey(secret)
                .parseClaimsJws(jwt);
    }

    public static void main(String[] args) {
        String jwt = buildJWT();
        Jws<Claims> result = parseJWT(jwt);

        System.out.println(result);
        System.out.println("id: " + result.getBody().getId());
    }
}
