package com.biblioteca.service;

import com.biblioteca.model.Usuario;
import com.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(@NonNull Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public @NonNull Usuario salvar(@NonNull Usuario usuario) {
        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            // Verificar se a senha já está criptografada (BCrypt começa com $2a$, $2b$ ou $2y$)
            if (!usuario.getSenha().startsWith("$2")) {
                usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
            }
        }
        return usuarioRepository.save(usuario);
    }

    public @NonNull Usuario atualizar(@NonNull Long id, @NonNull Usuario usuarioAtualizado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        usuario.setNome(usuarioAtualizado.getNome());
        usuario.setEmail(usuarioAtualizado.getEmail());
        usuario.setRole(usuarioAtualizado.getRole());
        usuario.setAtivo(usuarioAtualizado.getAtivo());
        
        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
            // Verificar se a senha já está criptografada (BCrypt começa com $2a$, $2b$ ou $2y$)
            if (!usuarioAtualizado.getSenha().startsWith("$2")) {
                usuario.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
            } else {
                usuario.setSenha(usuarioAtualizado.getSenha());
            }
        }
        
        return usuarioRepository.save(usuario);
    }

    public void excluir(@NonNull Long id) {
        usuarioRepository.deleteById(id);
    }

    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}

