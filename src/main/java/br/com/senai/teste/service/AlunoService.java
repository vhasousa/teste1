package br.com.senai.teste.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import br.com.senai.teste.model.Aluno;
import br.com.senai.teste.repository.AlunoRepository;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public Aluno cadastrar(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    public List<Aluno> listar() {
        return alunoRepository.findAll();
    }

    public Optional<Aluno> buscarPorId(Integer id) {
        return alunoRepository.findById(id);
    }

    public Optional<Aluno> atualizar(
            Integer id, Aluno novosDados) {

        Optional<Aluno> alunoEncontrado = alunoRepository.findById(id);

        if (alunoEncontrado.isEmpty()) {
            return Optional.empty();
        }

        Aluno aluno = alunoEncontrado.get();

        aluno.setNome(novosDados.getNome());
        aluno.setEmail(novosDados.getEmail());

        return Optional.of(alunoRepository.save(aluno));
    }

    public boolean excluir(Integer id) {

        if (!alunoRepository.existsById(id)) {
            return false;
        }

        alunoRepository.deleteById(id);
        return true;
    }
}