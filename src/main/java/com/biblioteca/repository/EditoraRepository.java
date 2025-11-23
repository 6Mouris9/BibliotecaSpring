package com.biblioteca.repository;

import com.biblioteca.model.Editora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EditoraRepository extends JpaRepository<Editora, Long> {
    List<Editora> findByNomeContainingIgnoreCase(String nome);
    
    @Query("SELECT e FROM Editora e WHERE e.nome LIKE %:nome%")
    List<Editora> buscarPorNome(@Param("nome") String nome);
}

