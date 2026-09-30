package br.com.senai.teste.dto;

import br.com.senai.teste.model.Usuario;

public class UsuarioResponse {

    private Integer id;
    private String login;

    public UsuarioResponse(Usuario usuario) {
        this.id = usuario.getId();
        this.login = usuario.getLogin();
    }

    public Integer getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }
}