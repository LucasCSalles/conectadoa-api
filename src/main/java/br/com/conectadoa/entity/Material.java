package br.com.conectadoa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "materiais")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 140, nullable = false)
    private String titulo;

    @Column(length = 1000, nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private CategoriaMaterial categoria;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private EstadoConservacao estadoConservacao;

    @Column(length = 120, nullable = false)
    private String regiao;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private PontoColeta pontoColeta;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private StatusMaterial status = StatusMaterial.DISPONIVEL;

    @Column(nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_doador_id", nullable = false)
    private Usuario usuarioDoador;
}
