package com.biblioteca.config;

import com.biblioteca.model.*;
import com.biblioteca.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AutorService autorService;

    @Autowired
    private EditoraService editoraService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private LivroService livroService;

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private DevolucaoService devolucaoService;

    @Override
    public void run(String... args) throws Exception {
        // Verificar se já existem dados
        if (usuarioService.listarTodos().isEmpty()) {
            criarUsuarios();
            criarAutores();
            criarEditoras();
            criarCategorias();
            criarLivros();
            criarEmprestimos();
        }
    }

    private void criarUsuarios() {
        Usuario admin = new Usuario();
        admin.setNome("Administrador");
        admin.setEmail("admin@biblioteca.com");
        admin.setSenha("admin123"); // Será criptografado no método salvar
        admin.setRole(Usuario.Role.ADMIN);
        admin.setAtivo(true);
        usuarioService.salvar(admin);

        Usuario bibliotecario = new Usuario();
        bibliotecario.setNome("Bibliotecário");
        bibliotecario.setEmail("bibliotecario@biblioteca.com");
        bibliotecario.setSenha("biblio123"); // Será criptografado no método salvar
        bibliotecario.setRole(Usuario.Role.BIBLIOTECARIO);
        bibliotecario.setAtivo(true);
        usuarioService.salvar(bibliotecario);

        Usuario user1 = new Usuario();
        user1.setNome("João Silva");
        user1.setEmail("joao@email.com");
        user1.setSenha("user123"); // Será criptografado no método salvar
        user1.setRole(Usuario.Role.USER);
        user1.setAtivo(true);
        usuarioService.salvar(user1);

        Usuario user2 = new Usuario();
        user2.setNome("Maria Santos");
        user2.setEmail("maria@email.com");
        user2.setSenha("user123"); // Será criptografado no método salvar
        user2.setRole(Usuario.Role.USER);
        user2.setAtivo(true);
        usuarioService.salvar(user2);
    }

    private void criarAutores() {
        List<Autor> autores = new ArrayList<>();
        
        Autor autor1 = new Autor();
        autor1.setNome("Machado de Assis");
        autor1.setNacionalidade("Brasileiro");
        autor1.setDataNascimento(LocalDate.of(1839, 6, 21));
        autor1.setBiografia("Escritor brasileiro, considerado o maior nome da literatura nacional.");
        autores.add(autor1);

        Autor autor2 = new Autor();
        autor2.setNome("Clarice Lispector");
        autor2.setNacionalidade("Brasileira");
        autor2.setDataNascimento(LocalDate.of(1920, 12, 10));
        autor2.setBiografia("Escritora e jornalista brasileira, considerada uma das mais importantes escritoras do século XX.");
        autores.add(autor2);

        Autor autor3 = new Autor();
        autor3.setNome("Jorge Amado");
        autor3.setNacionalidade("Brasileiro");
        autor3.setDataNascimento(LocalDate.of(1912, 8, 10));
        autor3.setBiografia("Escritor brasileiro, um dos mais famosos e traduzidos escritores brasileiros de todos os tempos.");
        autores.add(autor3);

        Autor autor4 = new Autor();
        autor4.setNome("George Orwell");
        autor4.setNacionalidade("Britânico");
        autor4.setDataNascimento(LocalDate.of(1903, 6, 25));
        autor4.setBiografia("Escritor, jornalista e ensaísta político britânico.");
        autores.add(autor4);

        Autor autor5 = new Autor();
        autor5.setNome("J.K. Rowling");
        autor5.setNacionalidade("Britânica");
        autor5.setDataNascimento(LocalDate.of(1965, 7, 31));
        autor5.setBiografia("Escritora britânica, autora da série Harry Potter.");
        autores.add(autor5);

        for (Autor autor : autores) {
            autorService.salvar(autor);
        }
    }

    private void criarEditoras() {
        List<Editora> editoras = new ArrayList<>();
        
        Editora editora1 = new Editora();
        editora1.setNome("Companhia das Letras");
        editora1.setEmail("contato@companhiadasletras.com.br");
        editora1.setTelefone("(11) 3707-3500");
        editora1.setEndereco("Rua Bandeira Paulista, 702 - São Paulo, SP");
        editoras.add(editora1);

        Editora editora2 = new Editora();
        editora2.setNome("Editora Globo");
        editora2.setEmail("contato@editoraglobo.com.br");
        editora2.setTelefone("(11) 3767-4000");
        editora2.setEndereco("Av. Paulista, 900 - São Paulo, SP");
        editoras.add(editora2);

        Editora editora3 = new Editora();
        editora3.setNome("Rocco");
        editora3.setEmail("contato@rocco.com.br");
        editora3.setTelefone("(21) 3525-2000");
        editora3.setEndereco("Rua da Ajuda, 35 - Rio de Janeiro, RJ");
        editoras.add(editora3);

        Editora editora4 = new Editora();
        editora4.setNome("Editora Record");
        editora4.setEmail("contato@record.com.br");
        editora4.setTelefone("(21) 2585-2000");
        editora4.setEndereco("Rua Argentina, 171 - Rio de Janeiro, RJ");
        editoras.add(editora4);

        for (Editora editora : editoras) {
            editoraService.salvar(editora);
        }
    }

    private void criarCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        
        Categoria cat1 = new Categoria();
        cat1.setNome("Romance");
        cat1.setDescricao("Livros de romance e ficção romântica");
        categorias.add(cat1);

        Categoria cat2 = new Categoria();
        cat2.setNome("Ficção Científica");
        cat2.setDescricao("Livros de ficção científica e fantasia");
        categorias.add(cat2);

        Categoria cat3 = new Categoria();
        cat3.setNome("Literatura Brasileira");
        cat3.setDescricao("Obras da literatura brasileira");
        categorias.add(cat3);

        Categoria cat4 = new Categoria();
        cat4.setNome("Drama");
        cat4.setDescricao("Livros dramáticos e tragédias");
        categorias.add(cat4);

        Categoria cat5 = new Categoria();
        cat5.setNome("Aventura");
        cat5.setDescricao("Livros de aventura e ação");
        categorias.add(cat5);

        for (Categoria categoria : categorias) {
            categoriaService.salvar(categoria);
        }
    }

    private void criarLivros() {
        List<Autor> autores = autorService.listarTodos();
        List<Editora> editoras = editoraService.listarTodos();
        List<Categoria> categorias = categoriaService.listarTodos();

        if (autores.isEmpty() || editoras.isEmpty() || categorias.isEmpty()) {
            return;
        }

        List<Livro> livros = new ArrayList<>();

        Livro livro1 = new Livro();
        livro1.setTitulo("Dom Casmurro");
        livro1.setIsbn("9788535910973");
        livro1.setAutor(autores.get(0));
        livro1.setEditora(editoras.get(0));
        livro1.setCategoria(categorias.get(2));
        livro1.setAnoPublicacao(1899);
        livro1.setQuantidade(10);
        livro1.setQuantidadeDisponivel(8);
        livro1.setDataAquisicao(LocalDate.now().minusMonths(6));
        livro1.setValor(new BigDecimal("29.90"));
        livro1.setDescricao("Romance de Machado de Assis, considerado uma das obras-primas da literatura brasileira.");
        livros.add(livro1);

        Livro livro2 = new Livro();
        livro2.setTitulo("A Hora da Estrela");
        livro2.setIsbn("9788535910974");
        livro2.setAutor(autores.get(1));
        livro2.setEditora(editoras.get(0));
        livro2.setCategoria(categorias.get(2));
        livro2.setAnoPublicacao(1977);
        livro2.setQuantidade(8);
        livro2.setQuantidadeDisponivel(6);
        livro2.setDataAquisicao(LocalDate.now().minusMonths(4));
        livro2.setValor(new BigDecimal("34.90"));
        livro2.setDescricao("Último romance publicado em vida por Clarice Lispector.");
        livros.add(livro2);

        Livro livro3 = new Livro();
        livro3.setTitulo("Capitães da Areia");
        livro3.setIsbn("9788535910975");
        livro3.setAutor(autores.get(2));
        livro3.setEditora(editoras.get(1));
        livro3.setCategoria(categorias.get(2));
        livro3.setAnoPublicacao(1937);
        livro3.setQuantidade(12);
        livro3.setQuantidadeDisponivel(10);
        livro3.setDataAquisicao(LocalDate.now().minusMonths(3));
        livro3.setValor(new BigDecimal("39.90"));
        livro3.setDescricao("Romance de Jorge Amado sobre meninos de rua em Salvador.");
        livros.add(livro3);

        Livro livro4 = new Livro();
        livro4.setTitulo("1984");
        livro4.setIsbn("9788535910976");
        livro4.setAutor(autores.get(3));
        livro4.setEditora(editoras.get(2));
        livro4.setCategoria(categorias.get(1));
        livro4.setAnoPublicacao(1949);
        livro4.setQuantidade(15);
        livro4.setQuantidadeDisponivel(12);
        livro4.setDataAquisicao(LocalDate.now().minusMonths(2));
        livro4.setValor(new BigDecimal("44.90"));
        livro4.setDescricao("Distopia clássica de George Orwell sobre totalitarismo.");
        livros.add(livro4);

        Livro livro5 = new Livro();
        livro5.setTitulo("Harry Potter e a Pedra Filosofal");
        livro5.setIsbn("9788535910977");
        livro5.setAutor(autores.get(4));
        livro5.setEditora(editoras.get(3));
        livro5.setCategoria(categorias.get(1));
        livro5.setAnoPublicacao(1997);
        livro5.setQuantidade(20);
        livro5.setQuantidadeDisponivel(18);
        livro5.setDataAquisicao(LocalDate.now().minusMonths(1));
        livro5.setValor(new BigDecimal("49.90"));
        livro5.setDescricao("Primeiro livro da série Harry Potter.");
        livros.add(livro5);

        for (Livro livro : livros) {
            livroService.salvar(livro);
        }
    }

    private void criarEmprestimos() {
        List<Livro> livros = livroService.listarTodos();
        List<Usuario> usuarios = usuarioService.listarTodos();

        if (livros.isEmpty() || usuarios.size() < 3) {
            return;
        }

        // Criar alguns empréstimos
        try {
            Usuario user1 = usuarios.stream().filter(u -> u.getEmail().equals("joao@email.com")).findFirst().orElse(usuarios.get(2));
            Usuario user2 = usuarios.stream().filter(u -> u.getEmail().equals("maria@email.com")).findFirst().orElse(usuarios.get(3));

            // Empréstimo ativo
            if (livros.size() > 0) {
                emprestimoService.criarEmprestimo(livros.get(0), user1, LocalDate.now().minusDays(3), LocalDate.now().plusDays(4));
            }

            // Empréstimo ativo
            if (livros.size() > 1) {
                emprestimoService.criarEmprestimo(livros.get(1), user2, LocalDate.now().minusDays(5), LocalDate.now().plusDays(2));
            }

            // Empréstimo atrasado
            if (livros.size() > 2) {
                Emprestimo emprestimoAtrasado = emprestimoService.criarEmprestimo(
                    livros.get(2), user1, LocalDate.now().minusDays(10), LocalDate.now().minusDays(3));
                emprestimoAtrasado.setStatus(Emprestimo.StatusEmprestimo.ATRASADO);
                emprestimoService.salvar(emprestimoAtrasado);
            }

            // Empréstimo devolvido
            if (livros.size() > 3) {
                Emprestimo emprestimoDevolvido = emprestimoService.criarEmprestimo(
                    livros.get(3), user2, LocalDate.now().minusDays(15), LocalDate.now().minusDays(8));
                devolucaoService.criarDevolucao(emprestimoDevolvido, LocalDate.now().minusDays(7), "Devolução em dia");
            }
        } catch (Exception e) {
            // Ignorar erros na criação de empréstimos
            System.err.println("Erro ao criar empréstimos: " + e.getMessage());
        }
    }
}

