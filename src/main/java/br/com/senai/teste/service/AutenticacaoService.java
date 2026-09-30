package br.com.senai.teste.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.senai.teste.model.Usuario;
import br.com.senai.teste.repository.UsuarioRepository;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public AutenticacaoService(
            UsuarioRepository usuarioRepository) {

        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByLogin(login);

        if (usuarioEncontrado.isEmpty()) {
            throw new UsernameNotFoundException(
                    "Usuário não encontrado");
        }

        Usuario usuario = usuarioEncontrado.get();

        return User
                .withUsername(usuario.getLogin())
                .password(usuario.getSenha())
                .roles("USER")
                .build();
    }
}