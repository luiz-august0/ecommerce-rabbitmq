---
description: Regras para criação e modificação de Eventos de domínio na arquitetura limpa do projeto
---

# Eventos de Domínio

## Localização
`<modulo>/src/main/java/mx/florinda/eats/<modulo>/core/domain/event/`

## O que é
Representa um fato ocorrido no domínio ou em um sistema externo que o módulo precisa tratar. São objetos imutáveis que carregam os dados do evento.

## Estrutura obrigatória
- **Java record** (imutável por natureza)
- **Sem anotações Spring** ou de qualquer framework
- Deve conter `eventId` como `UUID` para idempotência
- Deve conter `dataHora` como `LocalDateTime`
- Construtor compacto de conveniência pode gerar `eventId` e `dataHora` automaticamente

## Convenção de nome
`<AcaoOcorrida>Event.java`

Exemplos: `PagamentoConfirmadoEvent`, `PedidoCriadoEvent`, `EntregaRealizadaEvent`

## Dependências permitidas
- Tipos Java padrão (`java.util.*`, `java.time.*`, `java.math.*`)
- `core.domain.entity.*` (apenas se necessário carregar referência de entidade)
- `core.domain.enums.*`

## Dependências proibidas
- `inbound.*` — nunca
- `infrastructure.*` — nunca
- `org.springframework.*` — nunca
- `jakarta.*` — nunca

## Regras adicionais
- Eventos são consumidos por use cases via inbound listeners — nunca diretamente pelos listeners
- O mesmo record de evento é usado tanto para publicação quanto para consumo
- Campos devem ser suficientemente descritivos para identificar o fato ocorrido sem contexto extra

## Exemplo
```java
package mx.florinda.eats.pedidos.core.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PagamentoConfirmadoEvent(
        Long pagamentoId,
        BigDecimal valor,
        Long pedidoId,
        LocalDateTime dataHora,
        UUID eventId
) {
    // Construtor de conveniência — gera metadados automaticamente
    public PagamentoConfirmadoEvent(Long pagamentoId, BigDecimal valor, Long pedidoId) {
        this(pagamentoId, valor, pedidoId, LocalDateTime.now(), UUID.randomUUID());
    }
}
```
