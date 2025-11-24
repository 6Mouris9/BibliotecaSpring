package com.biblioteca.service;

import com.biblioteca.model.Livro;
import com.biblioteca.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    public Optional<Livro> buscarPorId(@NonNull Long id) {
        return livroRepository.findById(id);
    }

    public @NonNull Livro salvar(@NonNull Livro livro) {
        // Garantir que quantidade nunca seja null
        if (livro.getQuantidade() == null) {
            livro.setQuantidade(0);
        }
        
        // Se for um livro novo (sem ID) ou quantidadeDisponivel não foi definida
        // definir quantidadeDisponivel igual à quantidade
        if (livro.getId() == null || livro.getQuantidadeDisponivel() == null) {
            livro.setQuantidadeDisponivel(livro.getQuantidade());
        }
        
        return livroRepository.save(livro);
    }

    public @NonNull Livro atualizar(@NonNull Long id, @NonNull Livro livroAtualizado) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        
        // Garantir que quantidade nunca seja null
        if (livroAtualizado.getQuantidade() == null) {
            livroAtualizado.setQuantidade(0);
        }
        
        // Calcular diferença antes de atualizar
        int quantidadeAntiga = livro.getQuantidade() != null ? livro.getQuantidade() : 0;
        int quantidadeNova = livroAtualizado.getQuantidade();
        int quantidadeDisponivelAtual = livro.getQuantidadeDisponivel() != null ? livro.getQuantidadeDisponivel() : 0;
        
        livro.setTitulo(livroAtualizado.getTitulo());
        livro.setIsbn(livroAtualizado.getIsbn());
        livro.setAutor(livroAtualizado.getAutor());
        livro.setEditora(livroAtualizado.getEditora());
        livro.setCategoria(livroAtualizado.getCategoria());
        livro.setAnoPublicacao(livroAtualizado.getAnoPublicacao());
        livro.setQuantidade(quantidadeNova);
        livro.setDataAquisicao(livroAtualizado.getDataAquisicao());
        livro.setValor(livroAtualizado.getValor());
        livro.setDescricao(livroAtualizado.getDescricao());
        
        // Atualizar quantidade disponível se a quantidade total mudou
        int diferenca = quantidadeNova - quantidadeAntiga;
        if (diferenca > 0) {
            // Se aumentou a quantidade, adiciona a diferença à disponível
            livro.setQuantidadeDisponivel(quantidadeDisponivelAtual + diferenca);
        } else if (diferenca < 0) {
            // Se diminuiu a quantidade, ajusta a disponível (não pode ficar negativa)
            int novaDisponivel = quantidadeDisponivelAtual + diferenca;
            livro.setQuantidadeDisponivel(Math.max(0, novaDisponivel));
        }
        // Se não mudou, mantém a quantidade disponível atual
        
        return livroRepository.save(livro);
    }

    public void excluir(@NonNull Long id) {
        livroRepository.deleteById(id);
    }

    public List<Livro> buscarPorTituloOuIsbn(String titulo, String isbn) {
        return livroRepository.buscarPorTituloOuIsbn(titulo, isbn);
    }

    public List<Livro> buscarLivrosDisponiveis() {
        return livroRepository.buscarLivrosDisponiveis();
    }

    public void decrementarQuantidadeDisponivel(@NonNull Long livroId) {
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        if (livro.getQuantidadeDisponivel() > 0) {
            livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
            livroRepository.save(livro);
        } else {
            throw new RuntimeException("Livro não disponível para empréstimo");
        }
    }

    public void incrementarQuantidadeDisponivel(@NonNull Long livroId) {
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
        livroRepository.save(livro);
    }
}

