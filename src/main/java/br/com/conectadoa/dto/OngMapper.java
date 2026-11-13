package br.com.conectadoa.dto;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.conectadoa.entity.Ong;

@Component
public class OngMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public OngResponse toDto(Ong ong) {
        return modelMapper.map(ong, OngResponse.class);
    }

    public Ong toModel(OngRequest dto) {
        return modelMapper.map(dto, Ong.class);
    }
}
