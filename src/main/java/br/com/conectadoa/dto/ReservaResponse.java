package br.com.conectadoa.dto;

import br.com.conectadoa.entity.PontoColeta;
import br.com.conectadoa.entity.StatusReserva;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservaResponse {
    private Long id;
    private Long materialId;
    private String materialTitulo;
    private PontoColeta materialPontoColeta;
    private Long usuarioInteressadoId;
    private String usuarioInteressadoNome;
    private Long ongDestinatariaId;
    private String ongDestinatariaNome;
    private StatusReserva status;
    private LocalDateTime dataReserva;
    private LocalDateTime dataEntrega;
    private String observacao;
}
