package br.com.senai.teste.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.senai.teste.dto.UsuarioRequest;
import br.com.senai.teste.model.Usuario;
import br.com.senai.teste.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario cadastrar(UsuarioRequest dados) {

        if (usuarioRepository.findByLogin(dados.getLogin()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Este login já está cadastrado"
            );
        }

        String senhaProtegida =
                passwordEncoder.encode(dados.getSenha());

        Usuario usuario = new Usuario(
                dados.getLogin(),
                senhaProtegida
        );

        return usuarioRepository.save(usuario);
    }
}