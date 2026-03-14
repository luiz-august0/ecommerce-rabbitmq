---
description: Regras para criação e modificação de Exceções na arquitetura limpa do projeto
---

# Exceções

## Duas camadas de exceção

### 1. Exceções de Domínio — `core/exception/`
`<modulo>/src/main/java/mx/florinda/eats/<modulo>/core/exception/`

Representa erros de regra de negócio ou falhas no processamento de eventos de domínio.

**Estrutura obrigatória:**
- Classe que estende `RuntimeException`
- **Sem dependências de framework** (nem Spring, nem AMQP, nem JPA)
- Construtor que aceita `String message`

**Dependências permitidas:**
- Apenas `java.lang.*`

**Dependências proibidas:**
- `org.springframework.*` — nunca
- `infrastructure.*` — nunca
- `inbound.*` — nunca

**Exemplo:**
```java
package mx.florinda.eats.pedidos.core.exception;

public class EventExceptionHandler extends RuntimeException {

    public EventExceptionHandler(String message) {
        super(message);
    }
}
```

---

### 2. Mapeadores de Exceção — `infrastructure/exception/`
`<modulo>/src/main/java/mx/florinda/eats/<modulo>/infrastructure/exception/`

Captura exceções de domínio e as converte em exceções específicas do framework (AMQP, HTTP, etc.).

**Estrutura obrigatória:**
- Classe anotada com `@Configuration` ou `@ControllerAdvice`
- Importa a exceção de domínio de `core.exception`
- Lança exceção de infraestrutura correspondente (ex: `AmqpRejectAndDontRequeueException`)

**Dependências permitidas:**
- `core.exception.*`
- `org.springframework.*`
- `jakarta.*`
- Exceções do framework (AMQP, HTTP, etc.)

**Dependências proibidas:**
- `inbound.*` — nunca
- `core.domain.*` — nunca (somente `core.exception`)

**Exemplo:**
```java
package mx.florinda.eats.pedidos.infrastructure.exception;

import mx.florinda.eats.pedidos.core.exception.EventExceptionHandler;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(EventExceptionHandler.class)
    public void handle(EventExceptionHandler eventExceptionHandler) {
        throw new AmqpRejectAndDontRequeueException(eventExceptionHandler.getMessage());
    }
}
```

---

## Regra geral
- **O domínio lança, a infraestrutura traduz.** Nunca o contrário.
- Use cases lançam exceções de `core.exception`. A infraestrutura intercepta e converte.
- Nunca lançar `AmqpRejectAndDontRequeueException` ou similares dentro de use cases ou gateways.
