package com.biblioteca.service;

import com.biblioteca.model.Autor;
import com.biblioteca.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> listarTodos() {
        return autorRepository.findAll();
    }

    public Optional<Autor> buscarPorId(@NonNull Long id) {
        return autorRepository.findById(id);
    }

    public @NonNull Autor salvar(@NonNull Autor autor) {
        return autorRepository.save(autor);
    }

    public @NonNull Autor atualizar(@NonNull Long id, @NonNull Autor autorAtualizado) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));
        
        autor.setNome(autorAtualizado.getNome());
        autor.setBiografia(autorAtualizado.getBiografia());
        autor.setDataNascimento(autorAtualizado.getDataNascimento());
        autor.setNacionalidade(autorAtualizado.getNacionalidade());
        
        return autorRepository.save(autor);
    }

    public void excluir(@NonNull Long id) {
        autorRepository.deleteById(id);
    }

    public List<Autor> buscarPorNome(String nome) {
        return autorRepository.buscarPorNome(nome);
    }
}

