package br.com.conectadoa.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.conectadoa.entity.Material;
import br.com.conectadoa.entity.Reserva;
import br.com.conectadoa.entity.StatusMaterial;
import br.com.conectadoa.entity.StatusReserva;
import br.com.conectadoa.entity.Usuario;
import br.com.conectadoa.exception.AcessoNegadoException;
import br.com.conectadoa.exception.RecursoNaoEncontradoException;
import br.com.conectadoa.exception.RegraNegocioException;
import br.com.conectadoa.repository.MaterialRepository;
import br.com.conectadoa.repository.ReservaRepository;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository repository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OngService ongService;

    // Sem ongDestinatariaId: reserva para o usuário que está pedindo.
    // Com ongDestinatariaId: o doador destina o próprio material à ONG.
    @Transactional
    public Reserva criar(Reserva reserva, Long materialId, Long ongDestinatariaId, Long usuarioId) {
        Usuario solicitante = usuarioService.buscar(usuarioId);
        Material material = materialService.buscar(materialId);

        if (material.getStatus() != StatusMaterial.DISPONIVEL) {
            throw new RegraNegocioException("Este material não está disponível para reserva.");
        }

        boolean ehDoador = material.getUsuarioDoador().getId().equals(solicitante.getId());

        if (ongDestinatariaId == null) {
            if (ehDoador) {
                throw new RegraNegocioException("O doador não pode reservar o próprio material.");
            }
            reserva.setUsuarioInteressado(solicitante);
        } else {
            if (!ehDoador) {
                throw new AcessoNegadoException("Somente o doador pode destinar o material a uma ONG.");
            }
            reserva.setOngDestinataria(ongService.buscar(ongDestinatariaId));
        }

        reserva.setMaterial(material);
        reserva.setStatus(StatusReserva.ATIVA);
        material.setStatus(StatusMaterial.RESERVADO);
        materialRepository.save(material);
        return repository.save(reserva);
    }

    public List<Reserva> listar(Long usuarioInteressadoId) {
        if (usuarioInteressadoId == null) {
            return repository.findAll();
        }
        return repository.findByUsuarioInteressado_Id(usuarioInteressadoId);
    }

    public Reserva buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reserva " + id + " não encontrada."));
    }

    // status ENTREGUE confirma a entrega (somente o doador)
    @Transactional
    public Reserva atualizar(Long id, Reserva dados, Long usuarioId) {
        Reserva reserva = buscar(id);
        usuarioService.buscar(usuarioId);
        boolean ehDoador = ehDoador(reserva, usuarioId);

        if (!ehDoador && !ehInteressado(reserva, usuarioId)) {
            throw new AcessoNegadoException("Somente o doador ou a pessoa interessada podem alterar esta reserva.");
        }
        if (reserva.getStatus() == StatusReserva.ENTREGUE) {
            throw new RegraNegocioException("Reserva já entregue não pode ser alterada.");
        }

        if (dados.getObservacao() != null) {
            reserva.setObservacao(dados.getObservacao());
        }

        if (dados.getStatus() == StatusReserva.ENTREGUE) {
            if (!ehDoador) {
                throw new AcessoNegadoException("Somente o doador pode confirmar a entrega.");
            }
            reserva.setStatus(StatusReserva.ENTREGUE);
            reserva.setDataEntrega(LocalDateTime.now());
            reserva.getMaterial().setStatus(StatusMaterial.ENTREGUE);
            materialRepository.save(reserva.getMaterial());
        }
        return repository.save(reserva);
    }

    // cancelamento antes da entrega: o material volta a DISPONIVEL
    @Transactional
    public void excluir(Long id, Long usuarioId) {
        Reserva reserva = buscar(id);
        usuarioService.buscar(usuarioId);

        if (!ehDoador(reserva, usuarioId) && !ehInteressado(reserva, usuarioId)) {
            throw new AcessoNegadoException("Somente o doador ou a pessoa interessada podem cancelar esta reserva.");
        }
        if (reserva.getStatus() == StatusReserva.ENTREGUE) {
            throw new RegraNegocioException("Reserva já entregue não pode ser cancelada.");
        }

        reserva.getMaterial().setStatus(StatusMaterial.DISPONIVEL);
        materialRepository.save(reserva.getMaterial());
        repository.delete(reserva);
    }

    private boolean ehDoador(Reserva reserva, Long usuarioId) {
        return reserva.getMaterial().getUsuarioDoador().getId().equals(usuarioId);
    }

    private boolean ehInteressado(Reserva reserva, Long usuarioId) {
        return reserva.getUsuarioInteressado() != null
                && reserva.getUsuarioInteressado().getId().equals(usuarioId);
    }
}
