package com.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 2, max = 200, message = "Título deve ter entre 2 e 200 caracteres")
    @Column(nullable = false, length = 200)
    private String titulo;

    @Size(max = 20, message = "ISBN deve ter no máximo 20 caracteres")
    @Column(length = 20, unique = true)
    private String isbn;

    @NotNull(message = "Autor é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    @NotNull(message = "Editora é obrigatória")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "editora_id", nullable = false)
    private Editora editora;

    @NotNull(message = "Categoria é obrigatória")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(name = "ano_publicacao")
    private Integer anoPublicacao;

    @Min(value = 0, message = "Quantidade deve ser maior ou igual a zero")
    @Column(nullable = false)
    private Integer quantidade = 0;

    @Min(value = 0, message = "Quantidade disponível deve ser maior ou igual a zero")
    @Column(name = "quantidade_disponivel", nullable = false)
    private Integer quantidadeDisponivel = 0;

    @Column(name = "data_aquisicao")
    private LocalDate dataAquisicao;

    @Column(precision = 10, scale = 2)
    private BigDecimal valor;

    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    @Column(length = 1000)
    private String descricao;

    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL)
    private java.util.List<Emprestimo> emprestimos;

    public Livro() {
    }

    public Livro(String titulo, String isbn, Autor autor, Editora editora, Categoria categoria) {
        this.titulo = titulo;
        this.isbn = isbn;
        this.autor = autor;
        this.editora = editora;
        this.categoria = categoria;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Editora getEditora() {
        return editora;
    }

    public void setEditora(Editora editora) {
        this.editora = editora;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Integer getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(Integer quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public LocalDate getDataAquisicao() {
        return dataAquisicao;
    }

    public void setDataAquisicao(LocalDate dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public java.util.List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public void setEmprestimos(java.util.List<Emprestimo> emprestimos) {
        this.emprestimos = emprestimos;
    }
}

