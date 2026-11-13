package br.com.conectadoa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.conectadoa.dto.MaterialMapper;
import br.com.conectadoa.dto.MaterialRequest;
import br.com.conectadoa.dto.MaterialResponse;
import br.com.conectadoa.entity.CategoriaMaterial;
import br.com.conectadoa.service.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

// X-Usuario-Id: ID de quem executa a ação (o MVP não tem login)
@RestController
@Tag(name = "Materiais")
@RequestMapping("api/${api.version}/materiais")
public class MaterialController {

    @Autowired
    private MaterialService service;

    @Autowired
    private MaterialMapper materialMapper;

    @PostMapping
    @Operation(summary = "Cadastrar material para doação (doador = X-Usuario-Id)")
    public ResponseEntity<MaterialResponse> create(@RequestHeader("X-Usuario-Id") Long usuarioId,
                                                   @Valid @RequestBody MaterialRequest dtoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(materialMapper.toDto(service.criar(materialMapper.toModel(dtoRequest), usuarioId)));
    }

    @GetMapping
    @Operation(summary = "Listar materiais (filtro opcional por categoria)")
    public ResponseEntity<List<MaterialResponse>> findAll(@RequestParam(required = false) CategoriaMaterial categoria) {
        return ResponseEntity.ok(service.listar(categoria).stream().map(material -> materialMapper.toDto(material)).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar material por ID")
    public ResponseEntity<MaterialResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(materialMapper.toDto(service.buscar(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar material (somente o doador)")
    public ResponseEntity<MaterialResponse> update(@PathVariable Long id,
                                                   @RequestHeader("X-Usuario-Id") Long usuarioId,
                                                   @Valid @RequestBody MaterialRequest dtoRequest) {
        return ResponseEntity.ok(materialMapper.toDto(service.atualizar(id, materialMapper.toModel(dtoRequest), usuarioId)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir material (somente o doador, se DISPONIVEL)")
    public ResponseEntity<Void> deleteById(@PathVariable Long id, @RequestHeader("X-Usuario-Id") Long usuarioId) {
        service.excluir(id, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
