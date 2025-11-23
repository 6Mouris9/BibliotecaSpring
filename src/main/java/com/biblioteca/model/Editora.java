package com.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "editoras")
public class Editora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
    @Column(length = 200)
    private String endereco;

    @Size(max = 50, message = "Telefone deve ter no máximo 50 caracteres")
    @Column(length = 50)
    private String telefone;

    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    @Column(length = 100)
    private String email;

    @OneToMany(mappedBy = "editora", cascade = CascadeType.ALL)
    private List<Livro> livros;

    public Editora() {
    }

    public Editora(String nome, String endereco, String telefone, String email) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }
}

