package com.biblioteca.repository;

import com.biblioteca.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    List<Autor> findByNomeContainingIgnoreCase(String nome);
    
    @Query("SELECT a FROM Autor a WHERE a.nome LIKE %:nome%")
    List<Autor> buscarPorNome(@Param("nome") String nome);
}

