package com.biblioteca.controller;

import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Emprestimo.StatusEmprestimo;
import com.biblioteca.model.Livro;
import com.biblioteca.model.Usuario;
import com.biblioteca.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private LivroService livroService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listar(@RequestParam(required = false) String status,
                        @RequestParam(required = false) Long usuarioId,
                        Model model) {
        List<Emprestimo> emprestimos;
        if (status != null && !status.isEmpty()) {
            emprestimos = emprestimoService.buscarPorStatus(StatusEmprestimo.valueOf(status));
        } else if (usuarioId != null) {
            emprestimos = emprestimoService.buscarPorUsuario(usuarioId);
        } else {
            emprestimos = emprestimoService.listarTodos();
        }
        model.addAttribute("emprestimos", emprestimos);
        model.addAttribute("statusFiltro", status);
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "emprestimos/listar";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("emprestimo", new Emprestimo());
        model.addAttribute("livros", livroService.buscarLivrosDisponiveis());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "emprestimos/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Emprestimo emprestimo = emprestimoService.buscarPorId(id);
        model.addAttribute("emprestimo", emprestimo);
        model.addAttribute("livros", livroService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "emprestimos/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Emprestimo emprestimo, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("livros", livroService.buscarLivrosDisponiveis());
            model.addAttribute("usuarios", usuarioService.listarTodos());
            return "emprestimos/form";
        }
        try {
            Livro livro = livroService.buscarPorId(emprestimo.getLivro().getId())
                    .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
            Usuario usuario = usuarioService.buscarPorId(emprestimo.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
            emprestimoService.criarEmprestimo(livro, usuario, emprestimo.getDataEmprestimo(), emprestimo.getDataPrevistaDevolucao());
            redirectAttributes.addFlashAttribute("mensagem", "Empréstimo criado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            model.addAttribute("livros", livroService.buscarLivrosDisponiveis());
            model.addAttribute("usuarios", usuarioService.listarTodos());
            return "emprestimos/form";
        }
        return "redirect:/emprestimos";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute Emprestimo emprestimo,
                           BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("livros", livroService.listarTodos());
            model.addAttribute("usuarios", usuarioService.listarTodos());
            return "emprestimos/form";
        }
        emprestimoService.atualizar(id, emprestimo);
        redirectAttributes.addFlashAttribute("mensagem", "Empréstimo atualizado com sucesso!");
        return "redirect:/emprestimos";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            emprestimoService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagem", "Empréstimo excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/emprestimos";
    }

    @GetMapping("/atrasados")
    public String listarAtrasados(Model model) {
        List<Emprestimo> emprestimosAtrasados = emprestimoService.buscarEmprestimosAtrasados();
        model.addAttribute("emprestimos", emprestimosAtrasados);
        return "emprestimos/listar";
    }
}

