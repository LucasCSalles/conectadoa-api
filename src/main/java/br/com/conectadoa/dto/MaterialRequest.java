package br.com.conectadoa.dto;

import br.com.conectadoa.entity.CategoriaMaterial;
import br.com.conectadoa.entity.EstadoConservacao;
import br.com.conectadoa.entity.PontoColeta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MaterialRequest {

    @NotBlank
    private String titulo;

    @NotBlank
    private String descricao;

    @NotNull
    private CategoriaMaterial categoria;

    @NotNull
    private EstadoConservacao estadoConservacao;

    @NotBlank
    private String regiao;

    @NotNull
    private PontoColeta pontoColeta;
}
