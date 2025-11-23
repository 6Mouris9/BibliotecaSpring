package com.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "devolucoes")
public class Devolucao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Empréstimo é obrigatório")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emprestimo_id", nullable = false, unique = true)
    private Emprestimo emprestimo;

    @Column(name = "data_devolucao", nullable = false)
    private LocalDate dataDevolucao;

    @Column(name = "multa", precision = 10, scale = 2)
    private BigDecimal multa = BigDecimal.ZERO;

    @Column(name = "dias_atraso")
    private Integer diasAtraso = 0;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @Column(name = "data_registro", nullable = false)
    private LocalDateTime dataRegistro;

    @PrePersist
    protected void onCreate() {
        dataRegistro = LocalDateTime.now();
        if (dataDevolucao == null) {
            dataDevolucao = LocalDate.now();
        }
        if (emprestimo != null && emprestimo.getDataPrevistaDevolucao() != null) {
            if (dataDevolucao.isAfter(emprestimo.getDataPrevistaDevolucao())) {
                diasAtraso = (int) java.time.temporal.ChronoUnit.DAYS.between(
                    emprestimo.getDataPrevistaDevolucao(), dataDevolucao);
                // Multa de R$ 2,00 por dia de atraso
                multa = BigDecimal.valueOf(diasAtraso).multiply(BigDecimal.valueOf(2.0));
            }
        }
    }

    public Devolucao() {
    }

    public Devolucao(Emprestimo emprestimo, LocalDate dataDevolucao) {
        this.emprestimo = emprestimo;
        this.dataDevolucao = dataDevolucao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public BigDecimal getMulta() {
        return multa;
    }

    public void setMulta(BigDecimal multa) {
        this.multa = multa;
    }

    public Integer getDiasAtraso() {
        return diasAtraso;
    }

    public void setDiasAtraso(Integer diasAtraso) {
        this.diasAtraso = diasAtraso;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}

