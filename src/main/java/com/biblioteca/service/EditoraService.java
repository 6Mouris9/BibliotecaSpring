package com.biblioteca.service;

import com.biblioteca.model.Editora;
import com.biblioteca.repository.EditoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EditoraService {

    @Autowired
    private EditoraRepository editoraRepository;

    public List<Editora> listarTodos() {
        return editoraRepository.findAll();
    }

    public Optional<Editora> buscarPorId(@NonNull Long id) {
        return editoraRepository.findById(id);
    }

    public @NonNull Editora salvar(@NonNull Editora editora) {
        return editoraRepository.save(editora);
    }

    public @NonNull Editora atualizar(@NonNull Long id, @NonNull Editora editoraAtualizada) {
        Editora editora = editoraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Editora não encontrada"));
        
        editora.setNome(editoraAtualizada.getNome());
        editora.setEndereco(editoraAtualizada.getEndereco());
        editora.setTelefone(editoraAtualizada.getTelefone());
        editora.setEmail(editoraAtualizada.getEmail());
        
        return editoraRepository.save(editora);
    }

    public void excluir(@NonNull Long id) {
        editoraRepository.deleteById(id);
    }

    public List<Editora> buscarPorNome(String nome) {
        return editoraRepository.buscarPorNome(nome);
    }
}

