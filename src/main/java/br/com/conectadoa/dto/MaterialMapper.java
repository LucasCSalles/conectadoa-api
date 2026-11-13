package br.com.conectadoa.dto;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.conectadoa.entity.Material;

@Component
public class MaterialMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    // usuarioDoador.id -> usuarioDoadorId e usuarioDoador.nome -> usuarioDoadorNome
    public MaterialResponse toDto(Material material) {
        return modelMapper.map(material, MaterialResponse.class);
    }

    public Material toModel(MaterialRequest dto) {
        return modelMapper.map(dto, Material.class);
    }
}
