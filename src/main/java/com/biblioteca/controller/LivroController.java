package com.biblioteca.controller;

import com.biblioteca.model.Livro;
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
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @Autowired
    private AutorService autorService;

    @Autowired
    private EditoraService editoraService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String listar(@RequestParam(required = false) String titulo,
                        @RequestParam(required = false) String isbn,
                        Model model) {
        List<Livro> livros;
        if ((titulo != null && !titulo.isEmpty()) || (isbn != null && !isbn.isEmpty())) {
            livros = livroService.buscarPorTituloOuIsbn(titulo != null ? titulo : "", isbn != null ? isbn : "");
        } else {
            livros = livroService.listarTodos();
        }
        model.addAttribute("livros", livros);
        model.addAttribute("tituloFiltro", titulo);
        model.addAttribute("isbnFiltro", isbn);
        return "livros/listar";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("livro", new Livro());
        model.addAttribute("autores", autorService.listarTodos());
        model.addAttribute("editoras", editoraService.listarTodos());
        model.addAttribute("categorias", categoriaService.listarTodos());
        return "livros/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Livro livro = livroService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        model.addAttribute("livro", livro);
        model.addAttribute("autores", autorService.listarTodos());
        model.addAttribute("editoras", editoraService.listarTodos());
        model.addAttribute("categorias", categoriaService.listarTodos());
        return "livros/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Livro livro, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("autores", autorService.listarTodos());
            model.addAttribute("editoras", editoraService.listarTodos());
            model.addAttribute("categorias", categoriaService.listarTodos());
            return "livros/form";
        }
        livroService.salvar(livro);
        redirectAttributes.addFlashAttribute("mensagem", "Livro salvo com sucesso!");
        return "redirect:/livros";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute Livro livro,
                           BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("autores", autorService.listarTodos());
            model.addAttribute("editoras", editoraService.listarTodos());
            model.addAttribute("categorias", categoriaService.listarTodos());
            return "livros/form";
        }
        livroService.atualizar(id, livro);
        redirectAttributes.addFlashAttribute("mensagem", "Livro atualizado com sucesso!");
        return "redirect:/livros";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        livroService.excluir(id);
        redirectAttributes.addFlashAttribute("mensagem", "Livro excluído com sucesso!");
        return "redirect:/livros";
    }
}

