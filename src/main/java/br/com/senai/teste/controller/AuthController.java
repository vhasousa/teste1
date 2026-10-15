package br.com.senai.teste.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.senai.teste.dto.LoginRequest;
import br.com.senai.teste.dto.TokenResponse;
import br.com.senai.teste.service.TokenService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(
            AuthenticationManager authenticationManager,
            TokenService tokenService) {

        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody LoginRequest dados) {

        try {
            UsernamePasswordAuthenticationToken credenciais =
                    new UsernamePasswordAuthenticationToken(
                            dados.getLogin(),
                            dados.getSenha()
                    );

            Authentication autenticacao =
                    authenticationManager.authenticate(credenciais);

            String token = tokenService
                    .gerarToken(autenticacao.getName());

            return ResponseEntity.ok(
                    new TokenResponse(token)
            );

        } catch (AuthenticationException erro) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
    }
}
