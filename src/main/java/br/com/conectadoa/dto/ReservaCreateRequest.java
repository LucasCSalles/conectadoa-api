package br.com.conectadoa.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservaCreateRequest {

    @NotNull
    private Long materialId;

    // preencher apenas quando o doador destina o material a uma ONG
    private Long ongDestinatariaId;

    private String observacao;
}
