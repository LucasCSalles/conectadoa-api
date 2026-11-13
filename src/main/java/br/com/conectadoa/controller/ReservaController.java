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

import br.com.conectadoa.dto.ReservaCreateRequest;
import br.com.conectadoa.dto.ReservaMapper;
import br.com.conectadoa.dto.ReservaResponse;
import br.com.conectadoa.dto.ReservaUpdateRequest;
import br.com.conectadoa.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

// X-Usuario-Id: ID de quem executa a ação (o MVP não tem login)
@RestController
@Tag(name = "Reservas")
@RequestMapping("api/${api.version}/reservas")
public class ReservaController {

    @Autowired
    private ReservaService service;

    @Autowired
    private ReservaMapper reservaMapper;

    @PostMapping
    @Operation(summary = "Reservar material (para si ou, se doador, para uma ONG)")
    public ResponseEntity<ReservaResponse> create(@RequestHeader("X-Usuario-Id") Long usuarioId,
                                                  @Valid @RequestBody ReservaCreateRequest dtoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservaMapper.toDto(service.criar(reservaMapper.toModel(dtoRequest),
                        dtoRequest.getMaterialId(), dtoRequest.getOngDestinatariaId(), usuarioId)));
    }

    // ?usuarioInteressadoId= mostra as reservas da pessoa ("Meus materiais")
    @GetMapping
    @Operation(summary = "Listar reservas (filtro opcional por usuarioInteressadoId)")
    public ResponseEntity<List<ReservaResponse>> findAll(@RequestParam(required = false) Long usuarioInteressadoId) {
        return ResponseEntity.ok(service.listar(usuarioInteressadoId).stream().map(reserva -> reservaMapper.toDto(reserva)).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar reserva por ID")
    public ResponseEntity<ReservaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reservaMapper.toDto(service.buscar(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar reserva / confirmar entrega com status ENTREGUE (somente o doador)")
    public ResponseEntity<ReservaResponse> update(@PathVariable Long id,
                                                  @RequestHeader("X-Usuario-Id") Long usuarioId,
                                                  @Valid @RequestBody ReservaUpdateRequest dtoRequest) {
        return ResponseEntity.ok(reservaMapper.toDto(service.atualizar(id, reservaMapper.toModel(dtoRequest), usuarioId)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar reserva (material volta a DISPONIVEL)")
    public ResponseEntity<Void> deleteById(@PathVariable Long id, @RequestHeader("X-Usuario-Id") Long usuarioId) {
        service.excluir(id, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
