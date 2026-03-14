---
description: Regras para criação e modificação de Controllers REST (inbound HTTP) na arquitetura limpa do projeto
---

# Inbound HTTP — Controllers REST

## Localização
`<modulo>/src/main/java/mx/florinda/eats/<modulo>/inbound/http/`

## O que é
Adaptador de entrada que recebe requisições HTTP, delega para use cases e retorna DTOs como resposta.

## Estrutura obrigatória
- Anotada com `@RestController` e `@RequestMapping("/rota")`
- Injeção de use cases **exclusivamente pelo construtor**
- Métodos retornam `ResponseEntity<DTO>` ou `List<DTO>`
- **Conversão entidade → DTO acontece aqui**, não nos use cases

## Convenção de nome
`<Entidade>Controller.java`

Exemplos: `PedidoController`, `ClienteController`

## Dependências permitidas
- `core.domain.usecase.*`
- `core.dto.*`
- `core.domain.entity.*` (apenas para receber retorno do use case e converter para DTO)
- `org.springframework.web.bind.annotation.*`
- `org.springframework.http.ResponseEntity`

## Dependências proibidas
- `infrastructure.*` — nunca
- `core.gateway.*` — nunca acessar gateways diretamente
- `core.domain.event.*` — nunca (eventos são para inbound/event)
- Qualquer repositório concreto — nunca

## Padrão de resposta
- **Lista:** retorna `List<DTO>` diretamente
- **Recurso por ID:** retorna `ResponseEntity<DTO>` com `.map(...).orElse(ResponseEntity.notFound().build())`
- **Criação:** retorna `ResponseEntity.created(...).body(dto)`
- **Sem conteúdo:** retorna `ResponseEntity.noContent().build()`

## Regras adicionais
- Controllers não contêm lógica de negócio
- Validações de request body usam `@Valid` com anotações Bean Validation nas classes de request
- Nunca chame mais de um use case por método de controller sem justificativa clara de orquestração

## Exemplo
```java
package mx.florinda.eats.pedidos.inbound.http;

import mx.florinda.eats.pedidos.core.domain.usecase.BuscarPedidoPorIdUC;
import mx.florinda.eats.pedidos.core.domain.usecase.ListarPedidosUC;
import mx.florinda.eats.pedidos.core.dto.PedidoDTO;
import mx.florinda.eats.pedidos.core.domain.entity.Pedido;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final ListarPedidosUC listarPedidosUC;
    private final BuscarPedidoPorIdUC buscarPedidoPorIdUC;

    public PedidoController(ListarPedidosUC listarPedidosUC, BuscarPedidoPorIdUC buscarPedidoPorIdUC) {
        this.listarPedidosUC = listarPedidosUC;
        this.buscarPedidoPorIdUC = buscarPedidoPorIdUC;
    }

    @GetMapping
    public List<PedidoDTO> lista() {
        List<Pedido> pedidos = listarPedidosUC.execute();
        return pedidos.stream().map(PedidoDTO::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> porId(@PathVariable Long id) {
        return buscarPedidoPorIdUC.execute(id)
                .map(pedido -> ResponseEntity.ok(new PedidoDTO(pedido)))
                .orElse(ResponseEntity.notFound().build());
    }
}
```
