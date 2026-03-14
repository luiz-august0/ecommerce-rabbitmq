---
description: Regras para criação e modificação de classes de infraestrutura (config, repository) na arquitetura limpa do projeto
---

# Infrastructure

## Localização
`<modulo>/src/main/java/mx/florinda/eats/<modulo>/infrastructure/`

Subpacotes:
- `infrastructure/config/` — configurações técnicas (AMQP, beans de infraestrutura)
- `infrastructure/repository/` — implementações dos gateways via Spring Data JPA
- `infrastructure/exception/` — veja regra específica em `exception.md`

---

## infrastructure/config/

### O que é
Classes de configuração Spring que declaram beans de infraestrutura técnica (filas, exchanges, converters, datasources, etc.).

### Estrutura obrigatória
- Anotada com `@Configuration`
- Métodos de bean anotados com `@Bean`
- Constantes de nomes de recursos (`String`) como `public static final`

### Convenção de nome
`<Tecnologia>Config.java`

Exemplos: `AmqpConfig`, `RedisConfig`, `SecurityConfig`

### Dependências permitidas
- `org.springframework.*`
- Bibliotecas da tecnologia configurada (AMQP, Redis, etc.)

### Dependências proibidas
- `core.*` — nunca (config não conhece o domínio)
- `inbound.*` — nunca

### Regras adicionais
- Constantes de nomes de filas/exchanges/tópicos são definidas aqui e referenciadas via `AmqpConfig.NOME_DA_CONSTANTE` nos listeners
- Sem lógica de negócio

### Exemplo
```java
package mx.florinda.eats.pedidos.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    public static final String PAGAMENTO_CONFIRMADO_QUEUE = "pedidos.pagamento-confirmado";
    public static final String PAGAMENTO_CONFIRMADO_DLQ = PAGAMENTO_CONFIRMADO_QUEUE + ".dlq";
    public static final String PEDIDOS_DLX = "pedidos.dlx";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public Queue filaPagamentosConfirmadosPedido() {
        return QueueBuilder.durable(PAGAMENTO_CONFIRMADO_QUEUE)
                .deadLetterExchange(PEDIDOS_DLX)
                .deadLetterRoutingKey(PAGAMENTO_CONFIRMADO_DLQ)
                .build();
    }
}
```

---

## infrastructure/repository/

### O que é
Implementação concreta dos gateways usando Spring Data JPA. Conecta o contrato do domínio ao banco de dados.

### Estrutura obrigatória
- Interface que **estende simultaneamente** `JpaRepository<Entidade, ID>` e a interface gateway de `core.gateway`
- Anotada com `@Repository`
- Queries customizadas com `@Query` (JPQL)

### Convenção de nome
`<Entidade>Repository.java`

Exemplos: `PedidoRepository`, `ClienteRepository`

### Dependências permitidas
- `core.domain.entity.*`
- `core.gateway.*` (implementa a interface gateway)
- `org.springframework.data.jpa.repository.*`
- `org.springframework.stereotype.Repository`
- `org.springframework.data.jpa.repository.Query`

### Dependências proibidas
- `inbound.*` — nunca
- `core.domain.usecase.*` — nunca
- `core.dto.*` — nunca

### Regras adicionais
- A interface gateway correspondente em `core.gateway` define os métodos que o repositório deve implementar
- Use cases **nunca** injetam `PedidoRepository` diretamente — sempre injetam `PedidoRepositoryGTW`
- Use `join fetch` em queries que precisam carregar associações para evitar N+1

### Exemplo
```java
package mx.florinda.eats.pedidos.infrastructure.repository;

import mx.florinda.eats.pedidos.core.domain.entity.Pedido;
import mx.florinda.eats.pedidos.core.gateway.PedidoRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>, PedidoRepositoryGTW {

    @Query("select p from Pedido p join fetch p.itensPedido i join fetch i.itemCardapio ic order by p.id")
    List<Pedido> listaComItens();
}
```
