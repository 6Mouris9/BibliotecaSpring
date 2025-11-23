package com.biblioteca.controller;

import com.biblioteca.model.Usuario;
import com.biblioteca.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listar(@RequestParam(required = false) String email, Model model) {
        List<Usuario> usuarios;
        if (email != null && !email.isEmpty()) {
            usuarios = usuarioService.buscarPorEmail(email)
                    .map(List::of)
                    .orElse(List.of());
        } else {
            usuarios = usuarioService.listarTodos();
        }
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("emailFiltro", email);
        return "usuarios/listar";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        model.addAttribute("usuario", usuario);
        return "usuarios/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Usuario usuario, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            return "usuarios/form";
        }
        if (usuarioService.existePorEmail(usuario.getEmail())) {
            model.addAttribute("erro", "Email já cadastrado!");
            model.addAttribute("usuario", usuario);
            return "usuarios/form";
        }
        usuarioService.salvar(usuario);
        redirectAttributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso!");
        return "redirect:/usuarios";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute Usuario usuario,
                           BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            usuario.setId(id);
            model.addAttribute("usuario", usuario);
            return "usuarios/form";
        }
        // Verificar se o email já existe em outro usuário
        Usuario usuarioExistente = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if (!usuarioExistente.getEmail().equals(usuario.getEmail()) && 
            usuarioService.existePorEmail(usuario.getEmail())) {
            model.addAttribute("erro", "Email já cadastrado para outro usuário!");
            usuario.setId(id);
            model.addAttribute("usuario", usuario);
            return "usuarios/form";
        }
        usuarioService.atualizar(id, usuario);
        redirectAttributes.addFlashAttribute("mensagem", "Usuário atualizado com sucesso!");
        return "redirect:/usuarios";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.excluir(id);
        redirectAttributes.addFlashAttribute("mensagem", "Usuário excluído com sucesso!");
        return "redirect:/usuarios";
    }
}

