package com.biblioteca.service;

import com.biblioteca.model.Devolucao;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Emprestimo.StatusEmprestimo;
import com.biblioteca.repository.DevolucaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class DevolucaoService {

    @Autowired
    private DevolucaoRepository devolucaoRepository;

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private LivroService livroService;

    public List<Devolucao> listarTodos() {
        return devolucaoRepository.findAll();
    }

    public @NonNull Devolucao buscarPorId(@NonNull Long id) {
        return Objects.requireNonNull(devolucaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Devolução não encontrada")));
    }

    public Devolucao criarDevolucao(Emprestimo emprestimo, LocalDate dataDevolucao, String observacoes) {
        // Validar se o empréstimo está ativo
        if (emprestimo.getStatus() != StatusEmprestimo.ATIVO && 
            emprestimo.getStatus() != StatusEmprestimo.ATRASADO) {
            throw new RuntimeException("Empréstimo já foi devolvido ou está cancelado");
        }

        // Criar devolução
        Devolucao devolucao = new Devolucao();
        devolucao.setEmprestimo(emprestimo);
        devolucao.setDataDevolucao(dataDevolucao != null ? dataDevolucao : LocalDate.now());
        devolucao.setObservacoes(observacoes);

        // Calcular multa se houver atraso
        if (devolucao.getDataDevolucao().isAfter(emprestimo.getDataPrevistaDevolucao())) {
            long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(
                    emprestimo.getDataPrevistaDevolucao(), devolucao.getDataDevolucao());
            devolucao.setDiasAtraso((int) diasAtraso);
            devolucao.setMulta(java.math.BigDecimal.valueOf(diasAtraso * 2.0)); // R$ 2,00 por dia
        }

        // Atualizar status do empréstimo
        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);
        emprestimo.setDataDevolucao(devolucao.getDataDevolucao());
        emprestimoService.salvar(emprestimo);

        // Incrementar quantidade disponível do livro
        livroService.incrementarQuantidadeDisponivel(Objects.requireNonNull(emprestimo.getLivro().getId()));

        // Salvar devolução
        return devolucaoRepository.save(devolucao);
    }

    public @NonNull Devolucao salvar(@NonNull Devolucao devolucao) {
        return devolucaoRepository.save(devolucao);
    }

    public List<Devolucao> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return devolucaoRepository.findByDataDevolucaoBetween(inicio, fim);
    }

    public List<Devolucao> buscarPorUsuario(Long usuarioId) {
        return devolucaoRepository.buscarPorUsuario(usuarioId);
    }

    public java.math.BigDecimal calcularTotalMultas(LocalDate inicio, LocalDate fim) {
        java.math.BigDecimal total = devolucaoRepository.calcularTotalMultas(inicio, fim);
        return total != null ? total : java.math.BigDecimal.ZERO;
    }
}

