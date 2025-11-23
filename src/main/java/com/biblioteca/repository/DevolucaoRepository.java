package com.biblioteca.repository;

import com.biblioteca.model.Devolucao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DevolucaoRepository extends JpaRepository<Devolucao, Long> {
    List<Devolucao> findByDataDevolucaoBetween(LocalDate inicio, LocalDate fim);
    
    @Query("SELECT d FROM Devolucao d WHERE d.emprestimo.usuario.id = :usuarioId")
    List<Devolucao> buscarPorUsuario(@Param("usuarioId") Long usuarioId);
    
    @Query("SELECT SUM(d.multa) FROM Devolucao d WHERE d.dataDevolucao BETWEEN :inicio AND :fim")
    java.math.BigDecimal calcularTotalMultas(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}

