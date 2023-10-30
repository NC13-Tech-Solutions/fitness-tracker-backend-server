package com.nc13techsolutions.fitnesstrackerbackendserver.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nc13techsolutions.fitnesstrackerbackendserver.models.JustUserCredentials;

@Service
@PropertySource("application.properties")
public class JwtService {

    @Value("${jwt-secure-key}")
    private String SECURE_KEY;
    @Value("${jwt-key-issuer}")
    private String ISSUER;
    @Value("${jwt-key-expiration-time}")
    private long TOKEN_EXPIRATION_TIME;
    private static final String CLAIM_STRING = "UserCredentials";

    public JustUserCredentials extractCredentials(String token) {
        Claim claim = extractClaim(token);
        if (claim == null)
            return null;
        return getJustUserCredentialsFromString(claim.asString());
    }

    public String createJWTToken(JustUserCredentials userCredentials) {
        return JWT
                .create()
                .withIssuer(ISSUER)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plusMillis(TOKEN_EXPIRATION_TIME))
                .withSubject(userCredentials.getUSERNAME())
                .withClaim(CLAIM_STRING, userCredentials.toString())
                .sign(getSigningAlgorithm());
    }

    private Claim extractClaim(String token) {
        if (!isJWTTokenValid(token))
            return null;
        return getJwtVerifier().verify(token).getClaim(CLAIM_STRING);
    }

    private Algorithm getSigningAlgorithm() {
        Algorithm algorithm = Algorithm.HMAC256(SECURE_KEY);
        return algorithm;
    }

    public boolean isJWTTokenValid(String token) {
        try {
            DecodedJWT decodedJWT = getJwtVerifier().verify(token);
            if (decodedJWT.getExpiresAtAsInstant().isAfter(Instant.now()))
                return true;

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isJWTTokenVerified(String token, JustUserCredentials user) {
        JustUserCredentials tokenCredentials = extractCredentials(token);
        if (tokenCredentials != null) {
            if (tokenCredentials.getUSERNAME().equals(user.getUSERNAME()) &&
                    tokenCredentials.getPASSWORD().equals(tokenCredentials.getPASSWORD()))
                return true;

            return false;
        }
        return false;
    }

    private JWTVerifier getJwtVerifier() {
        return JWT
                .require(getSigningAlgorithm())
                .build();
    }

    private JustUserCredentials getJustUserCredentialsFromString(String text) {
        JustUserCredentials credentials = new JustUserCredentials();
        if (text.indexOf("USERNAME=") != -1 && text.indexOf("PASSWORD=") != -1) {
            String temp = text.substring(text.indexOf("USERNAME=") + "USERNAME=".length());
            credentials.setUSERNAME(temp.substring(0, temp.indexOf(",")));
            String ptemp = temp.substring(temp.indexOf("PASSWORD=") + "PASSWORD=".length());
            credentials.setPASSWORD(ptemp.substring(0, ptemp.indexOf(")")));
        }
        return credentials;
    }

}
