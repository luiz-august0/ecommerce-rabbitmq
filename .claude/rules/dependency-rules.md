---
description: Regras de dependência entre camadas da arquitetura limpa — o que pode e não pode importar o quê
---

# Regras de Dependência entre Camadas

## Direção obrigatória

```
inbound  ──────►  core  ◄──────  infrastructure
```

- `inbound` depende de `core`
- `infrastructure` depende de `core`
- `core` **não depende de ninguém** (exceto do próprio `core.*` e Java padrão)

---

## Matriz de dependências

| Origem                      | Pode importar                                              | Nunca importa                              |
|-----------------------------|------------------------------------------------------------|--------------------------------------------|
| `core.domain.entity`        | `core.domain.entity`, `core.domain.enums`, `jakarta.persistence`, Java padrão | `inbound.*`, `infrastructure.*`, `core.dto`, `core.gateway`, `core.usecase` |
| `core.domain.enums`         | Java padrão                                                | Tudo fora de `java.*`                      |
| `core.domain.event`         | `core.domain.entity`, `core.domain.enums`, Java padrão   | `inbound.*`, `infrastructure.*`, Spring, Jakarta |
| `core.domain.usecase`       | `core.domain.*`, `core.gateway.*`, `core.exception.*`, `@Service`, `@Transactional` | `inbound.*`, `infrastructure.*`, `core.dto` |
| `core.gateway`              | `core.domain.entity`, `core.domain.enums`, Java padrão   | `inbound.*`, `infrastructure.*`, Spring, Jakarta |
| `core.dto`                  | `core.domain.entity`, `core.domain.enums`, Java padrão   | `inbound.*`, `infrastructure.*`, `core.gateway`, `core.usecase` |
| `core.exception`            | Java padrão                                                | Tudo fora de `java.lang.*`                 |
| `inbound.http`              | `core.domain.usecase`, `core.dto`, `core.domain.entity`, Spring Web | `infrastructure.*`                         |
| `inbound.event`             | `core.domain.usecase`, `core.domain.event`, `infrastructure.config` (constantes), Spring AMQP | `infrastructure.repository.*`, `core.gateway.*` |
| `infrastructure.config`     | Spring, bibliotecas técnicas                               | `core.*`, `inbound.*`                      |
| `infrastructure.repository` | `core.domain.entity`, `core.gateway`, Spring Data JPA     | `inbound.*`, `core.usecase`, `core.dto`    |
| `infrastructure.exception`  | `core.exception`, Spring, AMQP                            | `inbound.*`, `core.domain.*`               |

---

## Violações mais comuns — nunca faça

1. **Use case injetando repositório concreto:**
   ```java
   // ERRADO
   private final PedidoRepository pedidoRepository; // implementação concreta

   // CORRETO
   private final PedidoRepositoryGTW pedidoRepositoryGTW; // interface gateway
   ```

2. **Controller acessando gateway diretamente:**
   ```java
   // ERRADO
   private final PedidoRepositoryGTW pedidoRepositoryGTW;

   // CORRETO
   private final ListarPedidosUC listarPedidosUC;
   ```

3. **Use case retornando DTO:**
   ```java
   // ERRADO
   public List<PedidoDTO> execute() { ... }

   // CORRETO
   public List<Pedido> execute() { ... }
   ```

4. **Conversão de DTO dentro do use case:**
   ```java
   // ERRADO (dentro de um UC)
   return pedidos.stream().map(PedidoDTO::new).toList();

   // CORRETO — conversão no controller
   List<Pedido> pedidos = listarPedidosUC.execute();
   return pedidos.stream().map(PedidoDTO::new).toList();
   ```

5. **Entidade importando DTO ou use case:**
   ```java
   // ERRADO
   import mx.florinda.eats.pedidos.core.dto.PedidoDTO;
   ```

6. **Exceção de infraestrutura lançada no use case:**
   ```java
   // ERRADO (dentro de UC)
   throw new AmqpRejectAndDontRequeueException("...");

   // CORRETO
   throw new EventExceptionHandler("..."); // exceção de domínio
   ```

7. **Listener com lógica de negócio:**
   ```java
   // ERRADO
   @RabbitListener(queues = "fila")
   public void consume(PagamentoConfirmadoEvent event) {
       Pedido pedido = repository.findById(event.pedidoId()).orElseThrow();
       pedido.setStatus(StatusPedido.PAGO);
       // lógica aqui é violação
   }

   // CORRETO
   @RabbitListener(queues = AmqpConfig.PAGAMENTO_CONFIRMADO_QUEUE)
   public void consume(PagamentoConfirmadoEvent event) {
       confirmaPagamentoPedidoUC.execute(event); // delega ao UC
   }
   ```
