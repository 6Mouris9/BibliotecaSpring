package com.biblioteca.controller;

import com.biblioteca.model.Devolucao;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.service.DevolucaoService;
import com.biblioteca.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/devolucoes")
public class DevolucaoController {

    @Autowired
    private DevolucaoService devolucaoService;

    @Autowired
    private EmprestimoService emprestimoService;

    @GetMapping
    public String listar(@RequestParam(required = false) Long usuarioId, Model model) {
        List<Devolucao> devolucoes;
        if (usuarioId != null) {
            devolucoes = devolucaoService.buscarPorUsuario(usuarioId);
        } else {
            devolucoes = devolucaoService.listarTodos();
        }
        model.addAttribute("devolucoes", devolucoes);
        return "devolucoes/listar";
    }

    @GetMapping("/novo/{emprestimoId}")
    public String novo(@PathVariable Long emprestimoId, Model model) {
        Emprestimo emprestimo = emprestimoService.buscarPorId(emprestimoId);
        Devolucao devolucao = new Devolucao();
        devolucao.setEmprestimo(emprestimo);
        devolucao.setDataDevolucao(LocalDate.now());
        model.addAttribute("devolucao", devolucao);
        return "devolucoes/form";
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam Long emprestimoId,
                        @RequestParam LocalDate dataDevolucao,
                        @RequestParam(required = false) String observacoes,
                        RedirectAttributes redirectAttributes) {
        try {
            Emprestimo emprestimo = emprestimoService.buscarPorId(emprestimoId);
            if (emprestimo == null) {
                redirectAttributes.addFlashAttribute("erro", "Empréstimo não encontrado!");
                return "redirect:/emprestimos";
            }
            devolucaoService.criarDevolucao(emprestimo, dataDevolucao, observacoes);
            redirectAttributes.addFlashAttribute("mensagem", "Devolução registrada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/emprestimos";
        }
        return "redirect:/devolucoes";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        Devolucao devolucao = devolucaoService.buscarPorId(id);
        model.addAttribute("devolucao", devolucao);
        return "devolucoes/detalhes";
    }
}

