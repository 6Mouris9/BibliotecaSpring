package com.biblioteca.controller;

import com.biblioteca.model.Autor;
import com.biblioteca.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @GetMapping
    public String listar(@RequestParam(required = false) String nome, Model model) {
        List<Autor> autores;
        if (nome != null && !nome.isEmpty()) {
            autores = autorService.buscarPorNome(nome);
        } else {
            autores = autorService.listarTodos();
        }
        model.addAttribute("autores", autores);
        model.addAttribute("nomeFiltro", nome);
        return "autores/listar";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("autor", new Autor());
        return "autores/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Autor autor = autorService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));
        model.addAttribute("autor", autor);
        return "autores/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Autor autor, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "autores/form";
        }
        autorService.salvar(autor);
        redirectAttributes.addFlashAttribute("mensagem", "Autor salvo com sucesso!");
        return "redirect:/autores";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute Autor autor, 
                           BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "autores/form";
        }
        autorService.atualizar(id, autor);
        redirectAttributes.addFlashAttribute("mensagem", "Autor atualizado com sucesso!");
        return "redirect:/autores";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        autorService.excluir(id);
        redirectAttributes.addFlashAttribute("mensagem", "Autor excluído com sucesso!");
        return "redirect:/autores";
    }
}

