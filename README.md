# Baozi Store

API da atividade prática de Desenvolvimento Web Back-End.

Fernando Santos Reis - RU 4648180

## Tecnologias

Java 17, Spring Boot 3.5.7, Spring Data JPA, Maven e banco relacional H2.
A API atende em http://localhost:8080. O H2 grava os dados no arquivo
`./data/baozi`. O projeto inclui Maven Wrapper.

## Organização

- `model`: Cliente, Produto e Pedido, com mapeamento JPA.
- `repository`: interfaces Spring Data JPA.
- `controller`: endpoints e tratamento de erros.
- `dto`: entrada e saída de pedidos usando os IDs do cliente e produto.

## Endpoints

| Entidade | Criar | Listar | Buscar | Excluir |
|---|---|---|---|---|
| Cliente | POST /clientes | GET /clientes | GET /clientes/{id} | DELETE /clientes/{id} |
| Produto | POST /produtos | GET /produtos | GET /produtos/{id} | DELETE /produtos/{id} |
| Pedido | POST /pedidos | GET /pedidos | GET /pedidos/{id} | DELETE /pedidos/{id} |

Criação retorna 201 e o cabeçalho Location; consultas retornam 200; exclusão retorna 204.
Dados inválidos retornam 400. IDs inexistentes retornam 404. Cliente ou produto
vinculado a pedido não pode ser excluído (409). Produto sem estoque também retorna 409.
`estoque` indica disponibilidade, não a quantidade de unidades armazenadas.

## Postman

A coleção `postman/Baozi-Store.postman_collection.json` contém as requisições
das três entidades. Os IDs gerados ficam nas variáveis da coleção.

Os dados usados no estudo de caso são Fernando Santos Reis4648180, Baozi de porco,
R$ 8,50 e três unidades. O pedido corresponde a R$ 25,50; total não é um campo da API.

## Validação

O projeto foi compilado e os três testes de integração passaram. Esses testes verificam
o fluxo das três entidades, IDs inexistentes, dados inválidos, produto indisponível
e proteção dos vínculos entre pedidos, clientes e produtos.

A coleção também foi executada contra a API em funcionamento pelo Postman CLI:
12 requisições e 15 verificações passaram, sem falhas.

## Repositório

Repositório: https://github.com/Fernando-SRS/baozi-store-backend-2026

Os arquivos de `target/` e `data/` ficam fora do controle de versão.
