---
description: Convenções de nomenclatura para todos os componentes da arquitetura limpa do projeto
---

# Convenções de Nomenclatura

## Sufixos por tipo de componente

| Tipo                         | Sufixo          | Exemplo                            |
|------------------------------|-----------------|------------------------------------|
| Caso de uso                  | `UC`            | `ListarPedidosUC`                  |
| Gateway (interface porta)    | `GTW`           | `PedidoRepositoryGTW`              |
| DTO de saída                 | `DTO`           | `PedidoDTO`, `ClienteDTO`          |
| Evento de domínio            | `Event`         | `PagamentoConfirmadoEvent`         |
| Exceção de domínio           | nome livre      | `EventExceptionHandler`            |
| Listener de mensageria       | `Listener`      | `PagamentoConfirmadoListener`      |
| Controller REST              | `Controller`    | `PedidoController`                 |
| Configuração de infra        | `Config`        | `AmqpConfig`                       |
| Repositório JPA              | `Repository`    | `PedidoRepository`                 |
| Mapeador de exceção infra    | `ExceptionHandler` | `ExceptionHandler`              |

---

## Nomenclatura de pacotes

| Camada                       | Pacote                                                  |
|------------------------------|---------------------------------------------------------|
| Entidades de domínio         | `core.domain.entity`                                   |
| Enumerações de domínio       | `core.domain.enums`                                    |
| Eventos de domínio           | `core.domain.event`                                    |
| Casos de uso                 | `core.domain.usecase`                                  |
| DTOs                         | `core.dto`                                             |
| Exceções de domínio          | `core.exception`                                       |
| Gateways (portas de saída)   | `core.gateway`                                         |
| Controllers REST             | `inbound.http`                                         |
| Listeners de mensageria      | `inbound.event`                                        |
| Configurações técnicas       | `infrastructure.config`                                |
| Repositórios JPA             | `infrastructure.repository`                            |
| Mapeadores de exceção        | `infrastructure.exception`                             |

---

## Nomenclatura de métodos

| Contexto                  | Padrão                          | Exemplo                    |
|---------------------------|---------------------------------|----------------------------|
| Método principal de UC    | `execute()`                     | `execute(Long id)`         |
| Listener RabbitMQ         | nome do evento em camelCase     | `pagamentoConfirmado(...)`  |
| Endpoints GET (lista)     | `lista()` ou `listar()`         | `lista()`                  |
| Endpoints GET (por ID)    | `porId(@PathVariable Long id)`  | `porId(1L)`                |
| Endpoints POST            | `criar(...)` ou nome da ação    | `criar(@RequestBody ...)`  |
| Métodos de gateway        | igual ao Spring Data ou descritivo | `listaComItens()`       |

---

## Nomenclatura de constantes de fila/exchange (AmqpConfig)

Padrão: `<modulo>.<recurso>` separado por ponto, todo em minúsculas com hífen.

```java
public static final String PAGAMENTO_CONFIRMADO_QUEUE = "pedidos.pagamento-confirmado";
public static final String PAGAMENTO_CONFIRMADO_DLQ   = PAGAMENTO_CONFIRMADO_QUEUE + ".dlq";
public static final String PEDIDOS_DLX                = "pedidos.dlx";
```

---

## Nomenclatura de novos módulos

O pacote base de cada módulo deve seguir:
```
mx.florinda.eats.<nome-do-modulo>
```

Exemplos: `mx.florinda.eats.pedidos`, `mx.florinda.eats.pagamentos`, `mx.florinda.eats.entregas`
