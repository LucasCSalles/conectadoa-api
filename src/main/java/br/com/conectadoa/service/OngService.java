package br.com.conectadoa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.conectadoa.entity.Ong;
import br.com.conectadoa.exception.RecursoNaoEncontradoException;
import br.com.conectadoa.repository.OngRepository;

@Service
public class OngService {

    @Autowired
    private OngRepository repository;

    public Ong criar(Ong ong) {
        return repository.save(ong);
    }

    public List<Ong> listar() {
        return repository.findAll();
    }

    public Ong buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("ONG " + id + " não encontrada."));
    }

    public Ong atualizar(Long id, Ong dados) {
        Ong ong = buscar(id);
        ong.setNome(dados.getNome());
        ong.setDescricao(dados.getDescricao());
        ong.setEmailContato(dados.getEmailContato());
        ong.setTelefoneContato(dados.getTelefoneContato());
        ong.setCidade(dados.getCidade());
        ong.setSite(dados.getSite());
        return repository.save(ong);
    }

    public void excluir(Long id) {
        repository.delete(buscar(id));
    }
}
