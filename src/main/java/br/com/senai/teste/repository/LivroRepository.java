
package br.com.senai.teste.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.senai.teste.model.Livro;

public interface LivroRepository 
        extends JpaRepository<Livro, Integer> {

}
