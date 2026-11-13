package br.com.conectadoa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.conectadoa.entity.Usuario;
import br.com.conectadoa.exception.RecursoNaoEncontradoException;
import br.com.conectadoa.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public Usuario criar(Usuario usuario) {
        return repository.save(usuario);
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public Usuario buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário " + id + " não encontrado."));
    }

    public Usuario atualizar(Long id, Usuario dados) {
        Usuario usuario = buscar(id);
        usuario.setNome(dados.getNome());
        usuario.setEmail(dados.getEmail());
        usuario.setTelefone(dados.getTelefone());
        usuario.setRegiao(dados.getRegiao());
        return repository.save(usuario);
    }

    public void excluir(Long id) {
        repository.delete(buscar(id));
    }
}
