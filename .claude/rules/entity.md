---
description: Regras para criação e modificação de Entidades de domínio na arquitetura limpa do projeto
---

# Entidades de Domínio

## Localização
`<modulo>/src/main/java/mx/florinda/eats/<modulo>/core/domain/entity/`

## O que é
Representa o modelo de negócio central da aplicação. Contém os dados e as regras que pertencem ao domínio.

## Estrutura obrigatória
- Anotadas com `@Entity` (entidades principais) ou `@Embeddable` (value objects embutidos)
- Getters e setters **explícitos** (sem Lombok)
- Sem lógica de infraestrutura ou de casos de uso

## Anotações JPA permitidas
- `@Entity`, `@Embeddable`
- `@Id`, `@GeneratedValue`
- `@Column`, `@Embedded`
- `@Enumerated`
- `@ManyToOne`, `@OneToMany`, `@ManyToMany`, `@OneToOne`
- `@Table` (quando necessário)

## Dependências permitidas
- `core.domain.entity.*` (referências entre entidades do mesmo módulo)
- `core.domain.enums.*`
- `jakarta.persistence.*`
- Tipos Java padrão (`java.util.*`, `java.time.*`, `java.math.*`, etc.)

## Dependências proibidas
- `inbound.*` — nunca
- `infrastructure.*` — nunca
- `core.dto.*` — nunca
- `core.gateway.*` — nunca
- `core.domain.usecase.*` — nunca
- `org.springframework.*` — nunca (exceto `@Transactional` se absolutamente necessário)

## Regras adicionais
- Entidades não conhecem DTOs e não se convertem para DTO
- Nenhuma lógica de serialização JSON nas entidades (sem `@JsonIgnore`, `@JsonProperty`, etc.)
- Value objects que são embutidos em entidades usam `@Embeddable` e são referenciados com `@Embedded`

## Exemplo — Entidade principal
```java
package mx.florinda.eats.pedidos.core.domain.entity;

import jakarta.persistence.*;
import mx.florinda.eats.pedidos.core.domain.enums.StatusPedido;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @Embedded
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itensPedido;

    // getters e setters explícitos
}
```

## Exemplo — Value Object (Embeddable)
```java
package mx.florinda.eats.pedidos.core.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Cliente {

    @Column(name = "nome_cliente")
    private String nome;

    @Column(name = "cpf_cliente")
    private String cpf;

    // getters e setters explícitos
}
```
