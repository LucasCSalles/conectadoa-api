package br.com.conectadoa.repository;

import br.com.conectadoa.entity.CategoriaMaterial;
import br.com.conectadoa.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByCategoria(CategoriaMaterial categoria);
}
