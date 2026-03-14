---
description: Regras para criação e modificação de Gateways (GTW) na arquitetura limpa do projeto
---

# Gateways

## Localização
`<modulo>/src/main/java/mx/florinda/eats/<modulo>/core/gateway/`

## O que é
Interface Java pura que define o **contrato de acesso a dados ou serviços externos** sem expor detalhes de implementação. É a "porta de saída" (output port) do domínio.

## Estrutura obrigatória
- **Interface** Java (nunca classe concreta)
- Sem anotações Spring, JPA, AMQP ou qualquer framework
- Métodos retornam apenas tipos de `core.domain.entity`, primitivos ou tipos Java padrão

## Convenção de nome
`<Entidade>RepositoryGTW.java` para persistência
`<Servico>GTW.java` para serviços externos

Exemplos: `PedidoRepositoryGTW`, `NotificacaoGTW`

## Dependências permitidas
- `core.domain.entity.*`
- `core.domain.enums.*`
- Tipos Java padrão (`java.util.*`, `java.time.*`, `java.math.*`, etc.)

## Dependências proibidas
- `inbound.*` — nunca
- `infrastructure.*` — nunca
- `org.springframework.*` — nunca
- `jakarta.persistence.*` — nunca
- `core.dto.*` — nunca

## Implementação
- Implementada **exclusivamente** em `infrastructure/repository/` (ou equivalente)
- Use cases injetam **somente a interface gateway**, nunca a implementação concreta
- A implementação JPA estende `JpaRepository<Entidade, ID>` e implementa a interface gateway simultaneamente

## Exemplo
```java
package mx.florinda.eats.pedidos.core.gateway;

import mx.florinda.eats.pedidos.core.domain.entity.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoRepositoryGTW {
    List<Pedido> listaComItens();
    Optional<Pedido> findById(Long id);
}
```

## Implementação correspondente (em infrastructure)
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
