package com.biblioteca.repository;

import com.biblioteca.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    Optional<Livro> findByIsbn(String isbn);
    List<Livro> findByTituloContainingIgnoreCase(String titulo);
    List<Livro> findByAutorId(Long autorId);
    List<Livro> findByEditoraId(Long editoraId);
    List<Livro> findByCategoriaId(Long categoriaId);
    List<Livro> findByQuantidadeDisponivelGreaterThan(Integer quantidade);
    
    @Query("SELECT l FROM Livro l WHERE l.titulo LIKE %:titulo% OR l.isbn LIKE %:isbn%")
    List<Livro> buscarPorTituloOuIsbn(@Param("titulo") String titulo, @Param("isbn") String isbn);
    
    @Query("SELECT l FROM Livro l WHERE l.quantidadeDisponivel > 0")
    List<Livro> buscarLivrosDisponiveis();
}

