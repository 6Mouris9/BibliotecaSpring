package com.biblioteca.service;

import com.biblioteca.model.Categoria;
import com.biblioteca.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodos() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorId(@NonNull Long id) {
        return categoriaRepository.findById(id);
    }

    public @NonNull Categoria salvar(@NonNull Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public @NonNull Categoria atualizar(@NonNull Long id, @NonNull Categoria categoriaAtualizada) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        
        categoria.setNome(categoriaAtualizada.getNome());
        categoria.setDescricao(categoriaAtualizada.getDescricao());
        
        return categoriaRepository.save(categoria);
    }

    public void excluir(@NonNull Long id) {
        categoriaRepository.deleteById(id);
    }

    public List<Categoria> buscarPorNome(String nome) {
        return categoriaRepository.buscarPorNome(nome);
    }
}

