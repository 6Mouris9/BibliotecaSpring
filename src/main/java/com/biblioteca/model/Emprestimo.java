package com.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "emprestimos")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Livro é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @NotNull(message = "Usuário é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    @Column(name = "data_prevista_devolucao", nullable = false)
    private LocalDate dataPrevistaDevolucao;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEmprestimo status = StatusEmprestimo.ATIVO;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @OneToOne(mappedBy = "emprestimo", cascade = CascadeType.ALL)
    private Devolucao devolucao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        if (dataEmprestimo == null) {
            dataEmprestimo = LocalDate.now();
        }
        if (dataPrevistaDevolucao == null) {
            dataPrevistaDevolucao = dataEmprestimo.plusDays(7); // 7 dias por padrão
        }
    }

    public Emprestimo() {
    }

    public Emprestimo(Livro livro, Usuario usuario, LocalDate dataEmprestimo, LocalDate dataPrevistaDevolucao) {
        this.livro = livro;
        this.usuario = usuario;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public StatusEmprestimo getStatus() {
        return status;
    }

    public void setStatus(StatusEmprestimo status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Devolucao getDevolucao() {
        return devolucao;
    }

    public void setDevolucao(Devolucao devolucao) {
        this.devolucao = devolucao;
    }

    public enum StatusEmprestimo {
        ATIVO, DEVOLVIDO, ATRASADO, CANCELADO
    }
}

