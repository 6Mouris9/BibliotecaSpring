package com.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @Size(max = 500, message = "Biografia deve ter no máximo 500 caracteres")
    @Column(length = 500)
    private String biografia;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Size(max = 50, message = "Nacionalidade deve ter no máximo 50 caracteres")
    @Column(length = 50)
    private String nacionalidade;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    private List<Livro> livros;

    public Autor() {
    }

    public Autor(String nome, String biografia, LocalDate dataNascimento, String nacionalidade) {
        this.nome = nome;
        this.biografia = biografia;
        this.dataNascimento = dataNascimento;
        this.nacionalidade = nacionalidade;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }
}

