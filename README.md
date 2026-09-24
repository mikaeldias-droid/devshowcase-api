# DevShowcase API

API RESTful desenvolvida em Java e Spring Boot para gerenciar e demonstrar perfis de desenvolvedores, seus projetos, tecnologias utilizadas e feedbacks. Projeto desenvolvido como atividade acadêmica.

---

## 🛠️ Tecnologias Utilizadas

- **Java 17** (ou 21)
- **Spring Boot 3.2.4**
- **Spring Data JPA**
- **Spring Web (REST API)**
- **Bean Validation (Jakarta Validation)**
- **PostgreSQL** (Banco de dados de produção/desenvolvimento)
- **H2 Database** (Utilizado em memória durante os testes automatizados)
- **Apache Maven**

---

## 🗄️ Como Configurar o PostgreSQL

1. **Instale o PostgreSQL** e certifique-se de que o serviço está em execução.
2. Criar o banco de dados `devshowcase_db`:
   ```sql
   CREATE DATABASE devshowcase_db;
   ```
3. O projeto utiliza as seguintes configurações de conexão padrão em `src/main/resources/application.properties`:
   - Host: `localhost`
   - Porta: `5432`
   - Banco de Dados: `devshowcase_db`
   - Usuário: `postgres`
   - Senha: `postgres`

4. Você também pode personalizar as credenciais definindo variáveis de ambiente antes de executar a aplicação:
   - `DB_HOST`: Host do PostgreSQL
   - `DB_PORT`: Porta do banco
   - `DB_NAME`: Nome do banco de dados
   - `DB_USER`: Usuário do banco
   - `DB_PASSWORD`: Senha do banco

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
- JDK 17 ou superior instalado
- Apache Maven instalado (ou utilizar o wrapper `./mvnw`)

### Execução em Desenvolvimento
1. Clone / navegue até a pasta do projeto:
   ```bash
   cd devshowcase-api
   ```
2. Compile o projeto e execute os testes automatizados:
   ```bash
   mvn clean test
   ```
3. Inicie a aplicação:
   ```bash
   mvn spring-boot:run
   ```
4. A API estará acessível em: `http://localhost:8080`

---

## 📌 Endpoints da API e Exemplos de Requisições

### 1. Perfis (`/api/profiles`)

#### Cadastrar Perfil
- **Método:** `POST`
- **URL:** `http://localhost:8080/api/profiles`
- **Status de Sucesso:** `201 Created`
- **Body (JSON):**
  ```json
  {
    "name": "Maria Silva",
    "email": "maria.silva@example.com",
    "bio": "Desenvolvedora Full Stack especializada em Java e React.",
    "githubUrl": "https://github.com/mariasilva",
    "linkedinUrl": "https://linkedin.com/in/mariasilva"
  }
  ```

#### Buscar Perfil por ID
- **Método:** `GET`
- **URL:** `http://localhost:8080/api/profiles/1`
- **Status de Sucesso:** `200 OK`
- **Resposta (JSON):**
  ```json
  {
    "id": 1,
    "name": "Maria Silva",
    "email": "maria.silva@example.com",
    "bio": "Desenvolvedora Full Stack especializada em Java e React.",
    "githubUrl": "https://github.com/mariasilva",
    "linkedinUrl": "https://linkedin.com/in/mariasilva",
    "projects": []
  }
  ```

---

### 2. Tecnologias (`/api/technologies`)

#### Cadastrar Tecnologia
- **Método:** `POST`
- **URL:** `http://localhost:8080/api/technologies`
- **Status de Sucesso:** `201 Created`
- **Body (JSON):**
  ```json
  {
    "name": "Spring Boot",
    "category": "Backend"
  }
  ```

#### Listar Tecnologias
- **Método:** `GET`
- **URL:** `http://localhost:8080/api/technologies`
- **Status de Sucesso:** `200 OK`
- **Resposta (JSON):**
  ```json
  [
    {
      "id": 1,
      "name": "Spring Boot",
      "category": "Backend"
    }
  ]
  ```

---

### 3. Projetos (`/api/projects`)

#### Cadastrar Projeto
- **Método:** `POST`
- **URL:** `http://localhost:8080/api/projects`
- **Status de Sucesso:** `201 Created`
- **Body (JSON):**
  ```json
  {
    "title": "DevShowcase API",
    "description": "API RESTful para catálogo de projetos de desenvolvedores.",
    "repositoryUrl": "https://github.com/mariasilva/devshowcase-api",
    "liveUrl": "https://devshowcase.example.com",
    "profileId": 1,
    "technologyIds": [1]
  }
  ```

#### Listar Projetos
- **Método:** `GET`
- **URL:** `http://localhost:8080/api/projects`
- **Status de Sucesso:** `200 OK`
- **Resposta (JSON):**
  ```json
  [
    {
      "id": 1,
      "title": "DevShowcase API",
      "description": "API RESTful para catálogo de projetos de desenvolvedores.",
      "repositoryUrl": "https://github.com/mariasilva/devshowcase-api",
      "liveUrl": "https://devshowcase.example.com",
      "profileId": 1,
      "profileName": "Maria Silva",
      "technologies": [
        {
          "id": 1,
          "name": "Spring Boot",
          "category": "Backend"
        }
      ],
      "feedbacks": []
    }
  ]
  ```

---

## ⚠️ Tratamento de Erros

A API retorna códigos HTTP apropriados e respostas de erro estruturadas:

- **`400 Bad Request`**: Dados inválidos ou violação de validação no DTO.
- **`404 Not Found`**: Perfil ou tecnologia com ID informado não encontrado.
- **`409 Conflict`**: E-mail ou Nome de Tecnologia duplicado.

#### Exemplo de Erro de Validação (`400 Bad Request`):
```json
{
  "timestamp": "2026-09-22T23:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validação nos campos informados",
  "path": "/api/profiles",
  "errors": [
    {
      "field": "email",
      "message": "Informe um e-mail válido"
    }
  ]
}
```
