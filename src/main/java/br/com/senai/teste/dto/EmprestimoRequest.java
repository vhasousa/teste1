package br.com.senai.teste.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public class EmprestimoRequest {

    @NotNull (message = "O ID do aluno é obrigatório")
    private Integer alunoId;

    @NotNull(message = "O ID do livro é obrigatório")
    private Integer livroId;

    @NotNull(message = "A data prevista é obrigatória")
    @Future(message = "A data prevista deve estar no futuro")
    private LocalDate dataPrevistaDevolucao;

    public EmprestimoRequest() {
    }

    public Integer getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Integer alunoId) {
        this.alunoId = alunoId;
    }

    public Integer getLivroId() {
        return livroId;
    }

    public void setLivroId(Integer livroId) {
        this.livroId = livroId;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(
            LocalDate dataPrevistaDevolucao) {

        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

}