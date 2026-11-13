package br.com.conectadoa.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsuarioResponse {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String regiao;
    private LocalDateTime dataCadastro;
}
