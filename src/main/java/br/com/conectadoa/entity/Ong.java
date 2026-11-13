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
@Table(name = "ongs")
public class Ong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 140, nullable = false)
    private String nome;

    @Column(length = 500, nullable = false)
    private String descricao;

    @Column(length = 160, nullable = false, unique = true)
    private String emailContato;

    @Column(length = 20, nullable = false)
    private String telefoneContato;

    @Column(length = 160, nullable = false)
    private String cidade;

    @Column(length = 250)
    private String site;

    @Column(nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();
}
