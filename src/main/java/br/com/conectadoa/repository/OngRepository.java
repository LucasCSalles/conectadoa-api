package br.com.conectadoa.repository;

import br.com.conectadoa.entity.Ong;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OngRepository extends JpaRepository<Ong, Long> {
}
