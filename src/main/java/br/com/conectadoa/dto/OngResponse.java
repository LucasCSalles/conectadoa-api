package br.com.conectadoa.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OngResponse {
    private Long id;
    private String nome;
    private String descricao;
    private String emailContato;
    private String telefoneContato;
    private String cidade;
    private String site;
    private LocalDateTime dataCadastro;
}
