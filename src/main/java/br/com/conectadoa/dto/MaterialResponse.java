package br.com.conectadoa.dto;

import br.com.conectadoa.entity.CategoriaMaterial;
import br.com.conectadoa.entity.EstadoConservacao;
import br.com.conectadoa.entity.PontoColeta;
import br.com.conectadoa.entity.StatusMaterial;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaterialResponse {
    private Long id;
    private String titulo;
    private String descricao;
    private CategoriaMaterial categoria;
    private EstadoConservacao estadoConservacao;
    private String regiao;
    private PontoColeta pontoColeta;
    private StatusMaterial status;
    private LocalDateTime dataCadastro;
    private Long usuarioDoadorId;
    private String usuarioDoadorNome;
}
