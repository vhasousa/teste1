package br.com.senai.teste.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.senai.teste.model.Livro;
import br.com.senai.teste.service.LivroService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    public ResponseEntity<Livro> cadastrar(@Valid @RequestBody Livro livro) {
        Livro livroCadastrado = livroService.cadastrar(livro);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(livroCadastrado);
    }

    @GetMapping
    public ResponseEntity<List<Livro>> listar() {
        List<Livro> livros = livroService.listar();

        return ResponseEntity.ok(livros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable Integer id) {
        Optional<Livro> livro = livroService.buscarPorId(id);

        if (livro.isPresent()) {
            return ResponseEntity.ok(livro.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody Livro novosDados) {

        Optional<Livro> livroAtualizado =
                livroService.atualizar(id, novosDados);

        if (livroAtualizado.isPresent()) {
            return ResponseEntity.ok(livroAtualizado.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        boolean excluido = livroService.excluir(id);

        if (excluido) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}