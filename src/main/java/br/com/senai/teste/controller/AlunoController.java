package br.com.senai.teste.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Optional;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.senai.teste.model.Aluno;
import br.com.senai.teste.service.AlunoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping
    public ResponseEntity<Aluno> cadastrar(
            @Valid @RequestBody Aluno aluno) {

        Aluno alunoCadastrado = alunoService.cadastrar(aluno);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(alunoCadastrado);
    }

    @GetMapping
    public ResponseEntity<List<Aluno>> listar() {

        List<Aluno> alunos = alunoService.listar();

        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarPorId(
            @PathVariable Integer id) {

        Optional<Aluno> aluno = alunoService.buscarPorId(id);

        if (aluno.isPresent()) {
            return ResponseEntity.ok(aluno.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizar(
            @PathVariable Integer id,
            @RequestBody Aluno novosDados) {

        Optional<Aluno> alunoAtualizado = alunoService.atualizar(id, novosDados);

        if (alunoAtualizado.isPresent()) {
            return ResponseEntity.ok(alunoAtualizado.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Integer id) {

        boolean excluido = alunoService.excluir(id);

        if (excluido) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
