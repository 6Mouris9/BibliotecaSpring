package com.biblioteca.controller;

import com.biblioteca.model.Categoria;
import com.biblioteca.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String listar(@RequestParam(required = false) String nome, Model model) {
        List<Categoria> categorias;
        if (nome != null && !nome.isEmpty()) {
            categorias = categoriaService.buscarPorNome(nome);
        } else {
            categorias = categoriaService.listarTodos();
        }
        model.addAttribute("categorias", categorias);
        model.addAttribute("nomeFiltro", nome);
        return "categorias/listar";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categorias/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Categoria categoria = categoriaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        model.addAttribute("categoria", categoria);
        return "categorias/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Categoria categoria, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "categorias/form";
        }
        categoriaService.salvar(categoria);
        redirectAttributes.addFlashAttribute("mensagem", "Categoria salva com sucesso!");
        return "redirect:/categorias";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute Categoria categoria,
                           BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "categorias/form";
        }
        categoriaService.atualizar(id, categoria);
        redirectAttributes.addFlashAttribute("mensagem", "Categoria atualizada com sucesso!");
        return "redirect:/categorias";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        categoriaService.excluir(id);
        redirectAttributes.addFlashAttribute("mensagem", "Categoria excluída com sucesso!");
        return "redirect:/categorias";
    }
}

