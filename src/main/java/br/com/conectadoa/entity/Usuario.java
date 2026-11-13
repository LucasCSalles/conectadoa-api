package br.com.conectadoa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 120, nullable = false)
    private String nome;

    @Column(length = 160, nullable = false, unique = true)
    private String email;

    @Column(length = 20, nullable = false)
    private String telefone;

    @Column(length = 120, nullable = false)
    private String regiao;

    @Column(nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();
}
