package br.com.conectadoa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.conectadoa.entity.CategoriaMaterial;
import br.com.conectadoa.entity.Material;
import br.com.conectadoa.entity.StatusMaterial;
import br.com.conectadoa.entity.Usuario;
import br.com.conectadoa.exception.AcessoNegadoException;
import br.com.conectadoa.exception.RecursoNaoEncontradoException;
import br.com.conectadoa.exception.RegraNegocioException;
import br.com.conectadoa.repository.MaterialRepository;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    // o material pertence a quem cadastra e começa DISPONIVEL
    public Material criar(Material material, Long usuarioId) {
        Usuario doador = usuarioService.buscar(usuarioId);
        material.setUsuarioDoador(doador);
        material.setStatus(StatusMaterial.DISPONIVEL);
        return repository.save(material);
    }

    public List<Material> listar(CategoriaMaterial categoria) {
        if (categoria == null) {
            return repository.findAll();
        }
        return repository.findByCategoria(categoria);
    }

    public Material buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Material " + id + " não encontrado."));
    }

    // somente o doador edita
    public Material atualizar(Long id, Material dados, Long usuarioId) {
        Material material = buscar(id);
        validarDoador(material, usuarioId);
        material.setTitulo(dados.getTitulo());
        material.setDescricao(dados.getDescricao());
        material.setCategoria(dados.getCategoria());
        material.setEstadoConservacao(dados.getEstadoConservacao());
        material.setRegiao(dados.getRegiao());
        material.setPontoColeta(dados.getPontoColeta());
        return repository.save(material);
    }

    // somente o doador exclui, e apenas se o material estiver DISPONIVEL
    public void excluir(Long id, Long usuarioId) {
        Material material = buscar(id);
        validarDoador(material, usuarioId);
        if (material.getStatus() != StatusMaterial.DISPONIVEL) {
            throw new RegraNegocioException("Somente material DISPONIVEL pode ser excluído.");
        }
        repository.delete(material);
    }

    private void validarDoador(Material material, Long usuarioId) {
        usuarioService.buscar(usuarioId);
        if (!material.getUsuarioDoador().getId().equals(usuarioId)) {
            throw new AcessoNegadoException("Somente o doador pode alterar este material.");
        }
    }
}
