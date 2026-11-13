package br.com.conectadoa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OngRequest {

    @NotBlank
    private String nome;

    @NotBlank
    private String descricao;

    @NotBlank
    @Email
    private String emailContato;

    @NotBlank
    private String telefoneContato;

    @NotBlank
    private String cidade;

    private String site;
}
