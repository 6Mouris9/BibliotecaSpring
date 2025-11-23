package com.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(nullable = false)
    private Boolean ativo = true;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Emprestimo> emprestimos;

    public Usuario() {
    }

    public Usuario(String nome, String email, String senha, Role role) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = role;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public void setEmprestimos(List<Emprestimo> emprestimos) {
        this.emprestimos = emprestimos;
    }

    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }

    public enum Role {
        ADMIN, USER, BIBLIOTECARIO
    }
}

