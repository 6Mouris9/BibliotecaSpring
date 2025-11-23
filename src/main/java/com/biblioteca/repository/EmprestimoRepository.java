package com.biblioteca.repository;

import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Emprestimo.StatusEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findByUsuarioId(Long usuarioId);
    List<Emprestimo> findByLivroId(Long livroId);
    List<Emprestimo> findByStatus(StatusEmprestimo status);
    List<Emprestimo> findByDataEmprestimoBetween(LocalDate inicio, LocalDate fim);
    List<Emprestimo> findByDataPrevistaDevolucaoBeforeAndStatus(LocalDate data, StatusEmprestimo status);
    
    @Query("SELECT e FROM Emprestimo e WHERE e.status = 'ATIVO' AND e.dataPrevistaDevolucao < :data")
    List<Emprestimo> buscarEmprestimosAtrasados(@Param("data") LocalDate data);
    
    @Query("SELECT COUNT(e) FROM Emprestimo e WHERE e.usuario.id = :usuarioId AND e.status = 'ATIVO'")
    Long contarEmprestimosAtivosPorUsuario(@Param("usuarioId") Long usuarioId);
}

