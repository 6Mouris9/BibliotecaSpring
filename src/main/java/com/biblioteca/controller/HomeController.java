package com.biblioteca.controller;

import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.LivroRepository;
import com.biblioteca.service.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private EmprestimoService emprestimoService;

    @GetMapping("/")
    public String home(Model model) {
        // Estatísticas para a página inicial
        long totalLivros = livroRepository.count();
        long livrosDisponiveis = livroRepository.buscarLivrosDisponiveis().size();
        long emprestimosAtivos = emprestimoRepository.findByStatus(com.biblioteca.model.Emprestimo.StatusEmprestimo.ATIVO).size();
        long emprestimosAtrasados = emprestimoService.buscarEmprestimosAtrasados().size();

        model.addAttribute("totalLivros", totalLivros);
        model.addAttribute("livrosDisponiveis", livrosDisponiveis);
        model.addAttribute("emprestimosAtivos", emprestimosAtivos);
        model.addAttribute("emprestimosAtrasados", emprestimosAtrasados);

        return "index";
    }
}

