package br.com.senai.teste.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public class RenovacaoRequest {

    @NotNull(message = "A nova data é obrigatória")
    @Future(message = "A nova data deve estar no futuro")
    private LocalDate novaDataPrevista;

    public RenovacaoRequest() {
    }

    public LocalDate getNovaDataPrevista() {
        return novaDataPrevista;
    }

    public void setNovaDataPrevista(
            LocalDate novaDataPrevista) {

        this.novaDataPrevista = novaDataPrevista;
    }
}