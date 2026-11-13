package br.com.conectadoa.dto;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.conectadoa.entity.Usuario;

@Component
public class UsuarioMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public UsuarioResponse toDto(Usuario usuario) {
        return modelMapper.map(usuario, UsuarioResponse.class);
    }

    public Usuario toModel(UsuarioRequest dto) {
        return modelMapper.map(dto, Usuario.class);
    }
}
