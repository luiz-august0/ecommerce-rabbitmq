---
description: Regras para criação e modificação de Event Listeners (inbound de mensageria) na arquitetura limpa do projeto
---

# Inbound Event — Listeners de Mensageria

## Localização
`<modulo>/src/main/java/mx/florinda/eats/<modulo>/inbound/event/`

## O que é
Adaptador de entrada que consome mensagens de filas (RabbitMQ) e delega o processamento para use cases.

## Estrutura obrigatória
- Anotada com `@Component`
- Método listener anotado com `@RabbitListener(queues = AmqpConfig.NOME_DA_FILA)`
- Injeção do use case **exclusivamente pelo construtor**
- O método listener recebe o evento de domínio e **apenas chama o use case**

## Convenção de nome
`<EventoOuvido>Listener.java`

Exemplos: `PagamentoConfirmadoListener`, `PedidoCriadoListener`

## Dependências permitidas
- `core.domain.usecase.*`
- `core.domain.event.*`
- `infrastructure.config.*` (somente para referenciar constantes de nomes de fila)
- `org.springframework.amqp.rabbit.annotation.RabbitListener`
- `org.springframework.stereotype.Component`

## Dependências proibidas
- `infrastructure.repository.*` — nunca acessar repositórios diretamente
- `core.gateway.*` — nunca acessar gateways diretamente
- `core.dto.*` — nunca
- Qualquer lógica de negócio dentro do listener

## Regras adicionais
- **Zero lógica de negócio no listener.** O listener é apenas um adaptador.
- O nome da fila deve ser referenciado como constante de `AmqpConfig` — nunca como string literal
- Erros de processamento devem ser tratados via exceções lançadas pelo use case e interceptadas pelo `infrastructure/exception/`
- Um listener por tipo de evento

## Exemplo
```java
package mx.florinda.eats.pedidos.inbound.event;

import mx.florinda.eats.pedidos.core.domain.event.PagamentoConfirmadoEvent;
import mx.florinda.eats.pedidos.core.domain.usecase.ConfirmaPagamentoPedidoUC;
import mx.florinda.eats.pedidos.infrastructure.config.AmqpConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PagamentoConfirmadoListener {

    private final ConfirmaPagamentoPedidoUC confirmaPagamentoPedidoUC;

    public PagamentoConfirmadoListener(ConfirmaPagamentoPedidoUC confirmaPagamentoPedidoUC) {
        this.confirmaPagamentoPedidoUC = confirmaPagamentoPedidoUC;
    }

    @RabbitListener(queues = AmqpConfig.PAGAMENTO_CONFIRMADO_QUEUE)
    public void pagamentoConfirmado(PagamentoConfirmadoEvent pagamentoConfirmadoEvent) {
        confirmaPagamentoPedidoUC.execute(pagamentoConfirmadoEvent);
    }
}
```
