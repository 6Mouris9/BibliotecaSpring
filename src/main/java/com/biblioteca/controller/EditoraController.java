package com.biblioteca.controller;

import com.biblioteca.model.Editora;
import com.biblioteca.service.EditoraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/editoras")
public class EditoraController {

    @Autowired
    private EditoraService editoraService;

    @GetMapping
    public String listar(@RequestParam(required = false) String nome, Model model) {
        List<Editora> editoras;
        if (nome != null && !nome.isEmpty()) {
            editoras = editoraService.buscarPorNome(nome);
        } else {
            editoras = editoraService.listarTodos();
        }
        model.addAttribute("editoras", editoras);
        model.addAttribute("nomeFiltro", nome);
        return "editoras/listar";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("editora", new Editora());
        return "editoras/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Editora editora = editoraService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Editora não encontrada"));
        model.addAttribute("editora", editora);
        return "editoras/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Editora editora, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "editoras/form";
        }
        editoraService.salvar(editora);
        redirectAttributes.addFlashAttribute("mensagem", "Editora salva com sucesso!");
        return "redirect:/editoras";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute Editora editora,
                           BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "editoras/form";
        }
        editoraService.atualizar(id, editora);
        redirectAttributes.addFlashAttribute("mensagem", "Editora atualizada com sucesso!");
        return "redirect:/editoras";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        editoraService.excluir(id);
        redirectAttributes.addFlashAttribute("mensagem", "Editora excluída com sucesso!");
        return "redirect:/editoras";
    }
}

