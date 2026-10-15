package br.com.senai.teste.service;

import java.time.Instant;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String gerarToken(String login) {

        Instant agora = Instant.now();
        Instant expiracao = agora.plusSeconds(3600);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("teste-api")
                .subject(login)
                .issuedAt(agora)
                .expiresAt(expiracao)
                .build();

        JwsHeader header = JwsHeader
                .with(MacAlgorithm.HS256)
                .build();

        JwtEncoderParameters parametros =
                JwtEncoderParameters.from(header, claims);

        return jwtEncoder
                .encode(parametros)
                .getTokenValue();
    }
}
