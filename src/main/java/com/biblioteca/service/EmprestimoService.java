package com.biblioteca.service;

import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Emprestimo.StatusEmprestimo;
import com.biblioteca.model.Livro;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.EmprestimoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private LivroService livroService;

    public List<Emprestimo> listarTodos() {
        return emprestimoRepository.findAll();
    }

    public @NonNull Emprestimo buscarPorId(@NonNull Long id) {
        return Objects.requireNonNull(emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado")));
    }

    public Emprestimo criarEmprestimo(Livro livro, Usuario usuario, LocalDate dataEmprestimo, LocalDate dataPrevistaDevolucao) {
        // Validar se o livro está disponível
        if (livro.getQuantidadeDisponivel() <= 0) {
            throw new RuntimeException("Livro não disponível para empréstimo");
        }

        // Validar limite de empréstimos ativos do usuário (máximo 5)
        Long emprestimosAtivos = emprestimoRepository.contarEmprestimosAtivosPorUsuario(Objects.requireNonNull(usuario.getId()));
        if (emprestimosAtivos >= 5) {
            throw new RuntimeException("Usuário atingiu o limite de 5 empréstimos ativos");
        }

        // Criar empréstimo
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setUsuario(usuario);
        emprestimo.setDataEmprestimo(dataEmprestimo != null ? dataEmprestimo : LocalDate.now());
        emprestimo.setDataPrevistaDevolucao(dataPrevistaDevolucao != null ? dataPrevistaDevolucao : 
                emprestimo.getDataEmprestimo().plusDays(7));
        emprestimo.setStatus(StatusEmprestimo.ATIVO);

        // Decrementar quantidade disponível do livro
        livroService.decrementarQuantidadeDisponivel(Objects.requireNonNull(livro.getId()));

        return emprestimoRepository.save(emprestimo);
    }

    public @NonNull Emprestimo salvar(@NonNull Emprestimo emprestimo) {
        return Objects.requireNonNull(emprestimoRepository.save(emprestimo));
    }

    public @NonNull Emprestimo atualizar(@NonNull Long id, @NonNull Emprestimo emprestimoAtualizado) {
        Emprestimo emprestimo = buscarPorId(id);
        
        emprestimo.setLivro(emprestimoAtualizado.getLivro());
        emprestimo.setUsuario(emprestimoAtualizado.getUsuario());
        emprestimo.setDataEmprestimo(emprestimoAtualizado.getDataEmprestimo());
        emprestimo.setDataPrevistaDevolucao(emprestimoAtualizado.getDataPrevistaDevolucao());
        emprestimo.setStatus(emprestimoAtualizado.getStatus());
        emprestimo.setObservacoes(emprestimoAtualizado.getObservacoes());
        
        return emprestimoRepository.save(emprestimo);
    }

    public void excluir(@NonNull Long id) {
        Emprestimo emprestimo = buscarPorId(id);
        if (emprestimo.getStatus() == StatusEmprestimo.ATIVO) {
            livroService.incrementarQuantidadeDisponivel(Objects.requireNonNull(emprestimo.getLivro().getId()));
        }
        emprestimoRepository.deleteById(id);
    }

    public List<Emprestimo> buscarPorUsuario(Long usuarioId) {
        return emprestimoRepository.findByUsuarioId(usuarioId);
    }

    public List<Emprestimo> buscarPorStatus(StatusEmprestimo status) {
        return emprestimoRepository.findByStatus(status);
    }

    public List<Emprestimo> buscarEmprestimosAtrasados() {
        return emprestimoRepository.buscarEmprestimosAtrasados(LocalDate.now());
    }

    public List<Emprestimo> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return emprestimoRepository.findByDataEmprestimoBetween(inicio, fim);
    }

    public void atualizarStatusAtrasados() {
        List<Emprestimo> emprestimosAtrasados = buscarEmprestimosAtrasados();
        for (Emprestimo emprestimo : emprestimosAtrasados) {
            if (emprestimo.getStatus() == StatusEmprestimo.ATIVO) {
                emprestimo.setStatus(StatusEmprestimo.ATRASADO);
                emprestimoRepository.save(emprestimo);
            }
        }
    }
}

