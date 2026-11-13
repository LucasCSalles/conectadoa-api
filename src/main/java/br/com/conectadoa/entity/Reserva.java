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
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // unique: o banco impede duas reservas para o mesmo material
    @ManyToOne(optional = false)
    @JoinColumn(name = "material_id", nullable = false, unique = true)
    private Material material;

    // destinatário: uma pessoa OU uma ONG
    @ManyToOne
    @JoinColumn(name = "usuario_interessado_id")
    private Usuario usuarioInteressado;

    @ManyToOne
    @JoinColumn(name = "ong_destinataria_id")
    private Ong ongDestinataria;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private StatusReserva status = StatusReserva.ATIVA;

    @Column(nullable = false)
    private LocalDateTime dataReserva = LocalDateTime.now();

    private LocalDateTime dataEntrega;

    @Column(length = 500)
    private String observacao;
}
