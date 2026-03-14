---
description: Regras para criação e modificação de DTOs (Data Transfer Objects) na arquitetura limpa do projeto
---

# DTOs (Data Transfer Objects)

## Localização
`<modulo>/src/main/java/mx/florinda/eats/<modulo>/core/dto/`

## O que é
Objeto de transferência de dados usado para expor informações do domínio para o mundo externo (HTTP responses, etc.) sem vazar entidades JPA.

## Estrutura obrigatória
- Classe Java simples, **sem anotações Spring**
- Construtor que recebe a entidade correspondente e popula os campos
- Getters e setters explícitos (sem Lombok)
- Campos são tipos primitivos, `String`, tipos Java padrão ou outros DTOs

## Convenção de nome
`<Entidade>DTO.java`

Exemplos: `PedidoDTO`, `ClienteDTO`, `ItemPedidoDTO`, `ItemCardapioDTO`

## Dependências permitidas
- `core.domain.entity.*` (somente no construtor, para conversão)
- `core.domain.enums.*` (apenas para chamar `.name()` ao converter)
- Tipos Java padrão (`java.util.*`, `java.time.*`, `java.math.*`, etc.)
- Outros DTOs do mesmo pacote `core.dto.*`

## Dependências proibidas
- `inbound.*` — nunca
- `infrastructure.*` — nunca
- `core.gateway.*` — nunca
- `core.domain.usecase.*` — nunca
- `org.springframework.*` — nunca

## Regras adicionais
- DTOs são **imutáveis conceitualmente** — criados uma vez a partir da entidade
- A conversão entidade → DTO acontece na camada `inbound`, não nos use cases
- DTOs não voltam para o domínio (não são parâmetros de use cases)
- Enums são convertidos para `String` via `.name()` no construtor

## Exemplo
```java
package mx.florinda.eats.pedidos.core.dto;

import mx.florinda.eats.pedidos.core.domain.entity.Pedido;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoDTO {

    private Long id;
    private LocalDateTime dataHora;
    private String status;
    private ClienteDTO cliente;
    private List<ItemPedidoDTO> itensPedido;

    public PedidoDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.dataHora = pedido.getDataHora();
        this.status = pedido.getStatus().name();
        this.cliente = new ClienteDTO(pedido.getCliente());
        this.itensPedido = pedido.getItensPedido().stream().map(ItemPedidoDTO::new).toList();
    }

    // getters e setters explícitos
}
```
