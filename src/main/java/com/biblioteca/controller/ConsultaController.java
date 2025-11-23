package com.biblioteca.controller;

import com.biblioteca.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private DevolucaoRepository devolucaoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Consulta com múltiplas tabelas e totalização
    @GetMapping("/emprestimos-detalhados")
    public String emprestimosDetalhados(
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            Model model) {
        
        final LocalDate inicio = dataInicio != null ? dataInicio : LocalDate.now().minusMonths(1);
        final LocalDate fim = dataFim != null ? dataFim : LocalDate.now();

        List<Object[]> resultados = emprestimoRepository.findAll().stream()
                .filter(e -> !e.getDataEmprestimo().isBefore(inicio) && !e.getDataEmprestimo().isAfter(fim))
                .map(e -> new Object[]{
                    e.getId(),
                    e.getLivro().getTitulo(),
                    e.getUsuario().getNome(),
                    e.getDataEmprestimo(),
                    e.getDataPrevistaDevolucao(),
                    e.getStatus().name(),
                    e.getLivro().getAutor().getNome(),
                    e.getLivro().getCategoria().getNome()
                })
                .toList();

        long totalEmprestimos = resultados.size();
        long emprestimosAtivos = resultados.stream()
                .filter(r -> r[5].equals("ATIVO"))
                .count();
        long emprestimosDevolvidos = resultados.stream()
                .filter(r -> r[5].equals("DEVOLVIDO"))
                .count();

        model.addAttribute("resultados", resultados);
        model.addAttribute("dataInicio", inicio);
        model.addAttribute("dataFim", fim);
        model.addAttribute("totalEmprestimos", totalEmprestimos);
        model.addAttribute("emprestimosAtivos", emprestimosAtivos);
        model.addAttribute("emprestimosDevolvidos", emprestimosDevolvidos);
        
        return "consultas/emprestimos-detalhados";
    }

    // Consulta com agrupamento
    @GetMapping("/emprestimos-por-categoria")
    public String emprestimosPorCategoria(
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            Model model) {
        
        final LocalDate inicio = dataInicio != null ? dataInicio : LocalDate.now().minusMonths(1);
        final LocalDate fim = dataFim != null ? dataFim : LocalDate.now();

        List<Object[]> resultados = emprestimoRepository.findAll().stream()
                .filter(e -> !e.getDataEmprestimo().isBefore(inicio) && !e.getDataEmprestimo().isAfter(fim))
                .collect(java.util.stream.Collectors.groupingBy(
                    e -> e.getLivro().getCategoria().getNome(),
                    java.util.stream.Collectors.counting()
                ))
                .entrySet().stream()
                .map(e -> new Object[]{e.getKey(), e.getValue()})
                .sorted((a, b) -> Long.compare((Long) b[1], (Long) a[1]))
                .toList();

        long totalGeral = resultados.stream()
                .mapToLong(r -> (Long) r[1])
                .sum();

        model.addAttribute("resultados", resultados);
        model.addAttribute("dataInicio", inicio);
        model.addAttribute("dataFim", fim);
        model.addAttribute("totalGeral", totalGeral);
        
        return "consultas/emprestimos-por-categoria";
    }

    // Consulta de pivot - Empréstimos por mês e categoria
    @GetMapping("/pivot-emprestimos")
    public String pivotEmprestimos(
            @RequestParam(required = false) Integer ano,
            Model model) {
        
        final int anoFiltro = ano != null ? ano : LocalDate.now().getYear();

        Map<String, Map<String, Long>> pivot = new java.util.HashMap<>();
        List<String> categorias = categoriaRepository.findAll().stream()
                .map(c -> c.getNome())
                .toList();
        List<String> meses = List.of("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", 
                                     "Jul", "Ago", "Set", "Out", "Nov", "Dez");

        for (String categoria : categorias) {
            pivot.put(categoria, new java.util.HashMap<>());
            for (String mes : meses) {
                pivot.get(categoria).put(mes, 0L);
            }
        }

        emprestimoRepository.findAll().stream()
                .filter(e -> e.getDataEmprestimo().getYear() == anoFiltro)
                .forEach(e -> {
                    String categoria = e.getLivro().getCategoria().getNome();
                    int mesIndex = e.getDataEmprestimo().getMonthValue() - 1;
                    String mes = meses.get(mesIndex);
                    pivot.get(categoria).put(mes, pivot.get(categoria).get(mes) + 1);
                });

        // Calcular totais por categoria
        Map<String, Long> totaisPorCategoria = new java.util.HashMap<>();
        for (String categoria : categorias) {
            long total = pivot.get(categoria).values().stream().mapToLong(Long::longValue).sum();
            totaisPorCategoria.put(categoria, total);
        }

        // Calcular totais por mês
        Map<String, Long> totaisPorMes = new java.util.HashMap<>();
        for (String mes : meses) {
            long total = 0;
            for (String categoria : categorias) {
                total += pivot.get(categoria).get(mes);
            }
            totaisPorMes.put(mes, total);
        }

        // Calcular total geral
        long totalGeral = totaisPorCategoria.values().stream().mapToLong(Long::longValue).sum();

        model.addAttribute("pivot", pivot);
        model.addAttribute("categorias", categorias);
        model.addAttribute("meses", meses);
        model.addAttribute("ano", anoFiltro);
        model.addAttribute("totaisPorCategoria", totaisPorCategoria);
        model.addAttribute("totaisPorMes", totaisPorMes);
        model.addAttribute("totalGeral", totalGeral);
        
        return "consultas/pivot-emprestimos";
    }

    // Consulta de multas e totalização
    @GetMapping("/multas")
    public String multas(
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            Model model) {
        
        if (dataInicio == null) {
            dataInicio = LocalDate.now().minusMonths(1);
        }
        if (dataFim == null) {
            dataFim = LocalDate.now();
        }

        List<Object[]> resultados = devolucaoRepository.findByDataDevolucaoBetween(dataInicio, dataFim).stream()
                .filter(d -> d.getMulta().compareTo(BigDecimal.ZERO) > 0)
                .map(d -> new Object[]{
                    d.getId(),
                    d.getEmprestimo().getLivro().getTitulo(),
                    d.getEmprestimo().getUsuario().getNome(),
                    d.getDataDevolucao(),
                    d.getDiasAtraso(),
                    d.getMulta()
                })
                .toList();

        BigDecimal totalMultas = devolucaoService.calcularTotalMultas(dataInicio, dataFim);
        int totalAtrasos = resultados.size();

        model.addAttribute("resultados", resultados);
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);
        model.addAttribute("totalMultas", totalMultas);
        model.addAttribute("totalAtrasos", totalAtrasos);
        
        return "consultas/multas";
    }

    @Autowired
    private com.biblioteca.service.DevolucaoService devolucaoService;
}

