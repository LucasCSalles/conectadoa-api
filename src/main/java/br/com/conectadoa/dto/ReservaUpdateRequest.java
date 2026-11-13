package br.com.conectadoa.dto;

import br.com.conectadoa.entity.StatusReserva;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservaUpdateRequest {

    @NotNull
    private StatusReserva status;

    private String observacao;
}
