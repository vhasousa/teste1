package br.com.senai.teste.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.senai.teste.dto.UsuarioRequest;
import br.com.senai.teste.dto.UsuarioResponse;
import br.com.senai.teste.model.Usuario;
import br.com.senai.teste.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(
            @Valid @RequestBody UsuarioRequest dados) {

        Usuario usuarioCadastrado =
                usuarioService.cadastrar(dados);

        UsuarioResponse usuarioResponse = new UsuarioResponse(usuarioCadastrado);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioResponse);
    }
}
