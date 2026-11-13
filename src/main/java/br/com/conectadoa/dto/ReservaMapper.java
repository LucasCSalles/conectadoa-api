package br.com.conectadoa.dto;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.conectadoa.entity.Reserva;

@Component
public class ReservaMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    // material.id -> materialId, usuarioInteressado.nome -> usuarioInteressadoNome, etc.
    public ReservaResponse toDto(Reserva reserva) {
        return modelMapper.map(reserva, ReservaResponse.class);
    }

    // material, pessoa e ONG são buscados e validados na ReservaService a partir dos IDs
    public Reserva toModel(ReservaCreateRequest dto) {
        Reserva reserva = new Reserva();
        reserva.setObservacao(dto.getObservacao());
        return reserva;
    }

    public Reserva toModel(ReservaUpdateRequest dto) {
        return modelMapper.map(dto, Reserva.class);
    }
}
