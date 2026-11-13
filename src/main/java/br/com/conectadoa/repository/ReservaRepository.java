package br.com.conectadoa.repository;

import br.com.conectadoa.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByUsuarioInteressado_Id(Long usuarioInteressadoId);
}
