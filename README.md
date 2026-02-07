# Stardew Crop Planner 👨‍🌾

O **Stardew Crop Planner** é um motor de otimização de lucro para o jogo
**Stardew Valley**.\
O sistema calcula o **Retorno sobre Investimento (ROI)** de diferentes
culturas, considerando tempo de crescimento, rebrota, estação do ano e
profissões do jogador, seguindo fielmente as regras do jogo.

------------------------------------------------------------------------

## 🎯 Objetivo

Auxiliar jogadores a escolherem as melhores culturas para plantar,
maximizando o lucro de forma estratégica e coerente com a mecânica
original do Stardew Valley.

------------------------------------------------------------------------

## 🚀 Tecnologias Utilizadas

-   **Java 21**
-   **Spring Boot 3.4.1**
-   **Spring Data JPA**
-   **PostgreSQL 15+**
-   **Docker & Docker Compose**
-   **Flyway**
-   **Lombok**

------------------------------------------------------------------------

## 🏗️ Estrutura do Projeto

Arquitetura em camadas, seguindo padrões tradicionais de mercado:

    src/main/java
    ├── controller/   # Endpoints da API REST
    ├── service/      # Regras de negócio e cálculo de ROI
    ├── entity/       # Entidades JPA
    ├── repository/   # Persistência de dados
    ├── dto/          # Objetos de transferência de dados
    ├── enums/        # Estações, Profissões, etc.

------------------------------------------------------------------------

## 🛠️ Como Executar

### Subir o banco de dados com Docker

``` bash
docker-compose up -d
```

### Executar a aplicação

Via IDE (IntelliJ / VS Code) ou Maven Wrapper:

``` bash
./mvnw spring-boot:run
```

------------------------------------------------------------------------

## 🔎 Exemplo de Uso da API

``` http
GET http://localhost:8080/api/otimizar/melhores-culturas?jogadorId=1&custoSemente=20
```

------------------------------------------------------------------------

## 📈 Funcionalidades

-   Configuração completa de backend e banco de dados
-   Modelagem de culturas e estado do jogador
-   Cálculo de lucro com suporte a culturas recorrentes
-   Aplicação de bônus da profissão **Cultivador**
-   Arredondamentos conforme regras oficiais do jogo

------------------------------------------------------------------------

## 📜 Licença

Projeto de uso educacional e pessoal.
