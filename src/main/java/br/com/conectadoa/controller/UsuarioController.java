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

import br.com.conectadoa.dto.UsuarioMapper;
import br.com.conectadoa.dto.UsuarioRequest;
import br.com.conectadoa.dto.UsuarioResponse;
import br.com.conectadoa.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Usuários")
@RequestMapping("api/${api.version}/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @PostMapping
    @Operation(summary = "Cadastrar usuário")
    public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody UsuarioRequest dtoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioMapper.toDto(service.criar(usuarioMapper.toModel(dtoRequest))));
    }

    @GetMapping
    @Operation(summary = "Listar usuários")
    public ResponseEntity<List<UsuarioResponse>> findAll() {
        return ResponseEntity.ok(service.listar().stream().map(usuario -> usuarioMapper.toDto(usuario)).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar usuário por ID")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioMapper.toDto(service.buscar(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário")
    public ResponseEntity<UsuarioResponse> update(@PathVariable Long id, @Valid @RequestBody UsuarioRequest dtoRequest) {
        return ResponseEntity.ok(usuarioMapper.toDto(service.atualizar(id, usuarioMapper.toModel(dtoRequest))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir usuário")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
