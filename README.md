# Baozi Store

API da atividade prática de Desenvolvimento Web Back-End.

Fernando Santos Reis - RU 4648180

## Execução

Requisito: JDK 17 ou superior. O projeto inclui Maven Wrapper, que baixa o Maven na primeira execução; é preciso acesso à internet.

Na pasta do projeto:

```text
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

No Linux ou macOS, use `./mvnw` no lugar de `.\mvnw.cmd`.
Se o Maven já estiver instalado, também é possível executar `mvn clean test` e `mvn spring-boot:run`.

A API atende em http://localhost:8080. O H2 grava os dados na pasta `data/`.
Não é necessário instalar MySQL. O banco começa vazio.

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

Importe `postman/Baozi-Store.postman_collection.json`. Execute as requisições em ordem.
Os IDs criados ficam nas variáveis da coleção, por isso não precisam começar em 1.
Capture os três POSTs, as três listagens, as três buscas por ID e os três DELETEs.
Cada captura deve mostrar método, URL, corpo enviado quando houver, status e resposta.
Faça os DELETEs por último, começando pelo pedido.

Os dados usados no estudo de caso são Fernando Santos Reis4648180, Baozi de porco,
R$ 8,50 e três unidades. O pedido corresponde a R$ 25,50; total não é um campo da API.

## Publicação do código

Crie um repositório na sua conta e publique esta pasta, sem `target/` e `data/`.
Copie o link real do repositório para a seção correspondente do documento.
