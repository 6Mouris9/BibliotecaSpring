# Sistema de Biblioteca

Sistema completo de gerenciamento de biblioteca desenvolvido com Java 21, Spring Boot, Spring Data JPA, Hibernate, MariaDB, Thymeleaf, Bootstrap e Spring Security.

## 📋 Requisitos Atendidos

✅ **Mínimo de 4 CRUDs** - Implementados CRUDs para: Autores, Editoras, Categorias, Livros, Empréstimos, Devoluções e Usuários

✅ **Uso de CSS/Framework CSS (Bootstrap)** - Interface completa com Bootstrap 5.3

✅ **Tabela e programação de movimentação** - Sistema completo de empréstimos e devoluções com lógica de negócio

✅ **Validações em formulários** - Validações usando Bean Validation

✅ **Página principal e menu** - Dashboard com estatísticas e menu lateral

✅ **Autenticação e autorização** - Spring Security com roles (ADMIN, BIBLIOTECARIO, USER)

✅ **Geração automática de dados** - DataLoader cria dados iniciais automaticamente

✅ **Filtro de dados nas telas** - Filtros implementados em todas as listagens

✅ **Consultas com múltiplas tabelas e totalização** - Consulta de empréstimos detalhados

✅ **Consulta com agrupamento** - Empréstimos agrupados por categoria

✅ **Consulta de pivot** - Tabela pivot de empréstimos por mês e categoria

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Hibernate**
- **MariaDB**
- **Thymeleaf**
- **Bootstrap 5.3**
- **Spring Security**
- **Bean Validation**

## 📦 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/biblioteca/
│   │   ├── config/          # Configurações (Security, DataLoader)
│   │   ├── controller/     # Controllers REST
│   │   ├── model/           # Entidades JPA
│   │   ├── repository/      # Repositories JPA
│   │   └── service/          # Services com lógica de negócio
│   └── resources/
│       ├── templates/       # Páginas Thymeleaf
│       └── application.properties
└── pom.xml
```

## 🗄️ Modelo de Dados

### Entidades Principais

- **Usuario** - Usuários do sistema (ADMIN, BIBLIOTECARIO, USER)
- **Autor** - Autores dos livros
- **Editora** - Editoras dos livros
- **Categoria** - Categorias dos livros
- **Livro** - Livros do acervo
- **Emprestimo** - Registro de empréstimos
- **Devolucao** - Registro de devoluções com cálculo de multas

## 🚀 Como Executar

### Pré-requisitos

1. Java 21 instalado
2. Maven instalado
3. MariaDB instalado e rodando

### Configuração do Banco de Dados

1. Crie o banco de dados (ou deixe o sistema criar automaticamente):
```sql
CREATE DATABASE biblioteca_db;
```

2. Configure as credenciais no arquivo `application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=sua_senha
```

### Executando o Projeto

1. Clone o repositório ou navegue até a pasta do projeto

2. Execute o projeto:
```bash
mvn spring-boot:run
```

3. Acesse no navegador:
```
http://localhost:8080
```

## 👤 Usuários Padrão

O sistema cria automaticamente os seguintes usuários:

- **Administrador**
  - Email: `admin@biblioteca.com`
  - Senha: `admin123`
  - Role: ADMIN

- **Bibliotecário**
  - Email: `bibliotecario@biblioteca.com`
  - Senha: `biblio123`
  - Role: BIBLIOTECARIO

- **Usuário 1**
  - Email: `joao@email.com`
  - Senha: `user123`
  - Role: USER

- **Usuário 2**
  - Email: `maria@email.com`
  - Senha: `user123`
  - Role: USER

## 📊 Funcionalidades

### CRUDs Implementados

1. **Autores** - Gerenciamento de autores
2. **Editoras** - Gerenciamento de editoras
3. **Categorias** - Gerenciamento de categorias
4. **Livros** - Gerenciamento do acervo
5. **Empréstimos** - Controle de empréstimos
6. **Devoluções** - Registro de devoluções
7. **Usuários** - Gerenciamento de usuários (apenas ADMIN)

### Movimentações

- **Empréstimo**: 
  - Valida disponibilidade do livro
  - Limite de 5 empréstimos ativos por usuário
  - Prazo padrão de 7 dias
  - Decrementa quantidade disponível

- **Devolução**:
  - Calcula dias de atraso automaticamente
  - Aplica multa de R$ 2,00 por dia de atraso
  - Incrementa quantidade disponível
  - Atualiza status do empréstimo

### Consultas Avançadas

1. **Empréstimos Detalhados** - Consulta com múltiplas tabelas e totalização
2. **Empréstimos por Categoria** - Agrupamento com percentuais
3. **Pivot de Empréstimos** - Tabela pivot por mês e categoria
4. **Multas e Atrasos** - Relatório de multas com totalização

## 🔒 Segurança

- Autenticação via Spring Security
- Autorização por roles:
  - **ADMIN**: Acesso total
  - **BIBLIOTECARIO**: Acesso a consultas e movimentações
  - **USER**: Acesso básico
- Senhas criptografadas com BCrypt

## 📝 Validações

- Validação de campos obrigatórios
- Validação de formato de email
- Validação de tamanho mínimo de senha
- Validação de disponibilidade de livros
- Validação de limite de empréstimos
