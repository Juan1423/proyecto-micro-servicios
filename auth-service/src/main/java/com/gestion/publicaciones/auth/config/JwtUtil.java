package com.gestion.publicaciones.auth.config;


import com.nimbusds.jose.jwk.RSAKey;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.*;

@Component
public class JwtUtil {

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    private RSAKey rsaJwk;

    @PostConstruct
    public void loadKeys() throws Exception {
        InputStream privStream = getClass().getClassLoader().getResourceAsStream("keys/private_key.pem");
        InputStream pubStream = getClass().getClassLoader().getResourceAsStream("keys/public_key.pem");

        // Leer claves con KeyFactory (usa BouncyCastle o similar si da error con PEM)
        privateKey = (RSAPrivateKey) PemUtils.readPrivateKey(privStream);
        publicKey = (RSAPublicKey) PemUtils.readPublicKey(pubStream);

        rsaJwk = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID("auth-key-id")
                .build();
    }

    public String generateToken(UUID userId, Set<String> roles) throws JOSEException {
        JWSSigner signer = new RSASSASigner(privateKey);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userId.toString())
                .claim("roles", roles)
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 3600 * 1000))
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID("auth-key-id").build(),
                claimsSet
        );

        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    public RSAKey getJwk() {
        return rsaJwk.toPublicJWK();
    }
}
