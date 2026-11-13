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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.conectadoa.dto.OngMapper;
import br.com.conectadoa.dto.OngRequest;
import br.com.conectadoa.dto.OngResponse;
import br.com.conectadoa.service.OngService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "ONGs")
@RequestMapping("api/${api.version}/ongs")
public class OngController {

    @Autowired
    private OngService service;

    @Autowired
    private OngMapper ongMapper;

    @PostMapping
    @Operation(summary = "Cadastrar ONG")
    public ResponseEntity<OngResponse> create(@Valid @RequestBody OngRequest dtoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ongMapper.toDto(service.criar(ongMapper.toModel(dtoRequest))));
    }

    @GetMapping
    @Operation(summary = "Listar ONGs")
    public ResponseEntity<List<OngResponse>> findAll() {
        return ResponseEntity.ok(service.listar().stream().map(ong -> ongMapper.toDto(ong)).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar ONG por ID")
    public ResponseEntity<OngResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ongMapper.toDto(service.buscar(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar ONG")
    public ResponseEntity<OngResponse> update(@PathVariable Long id, @Valid @RequestBody OngRequest dtoRequest) {
        return ResponseEntity.ok(ongMapper.toDto(service.atualizar(id, ongMapper.toModel(dtoRequest))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir ONG")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
