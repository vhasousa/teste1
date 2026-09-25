package br.com.senai.teste.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.senai.teste.model.Aluno;

public interface AlunoRepository 
        extends JpaRepository<Aluno, Integer> {

}