package ru.maxima.springboottest.ProjectSpringBoot1.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTutil {

    @Value("${jwt_secret}")
    public String secret;

    public String generateToken(String username) {

        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject("Details about user")
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withIssuer("Viktor from Maxima")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }


    public String validationToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("Details about user")
                .withIssuer("Viktor from Maxima")
                .build();

        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaim("username").asString();

    }

}
