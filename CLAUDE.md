# Arquitetura Limpa — Regras do Projeto

Este projeto segue **Clean Architecture** com Spring Boot. As regras abaixo são obrigatórias ao criar ou modificar código em qualquer módulo.

---

## Estrutura de Pacotes

```
<modulo>/src/main/java/mx/florinda/eats/<modulo>/
├── core/
│   ├── domain/
│   │   ├── entity/       # Entidades de domínio
│   │   ├── enums/        # Enumerações de domínio
│   │   ├── event/        # Eventos de domínio (records)
│   │   └── usecase/      # Casos de uso da aplicação
│   ├── dto/              # DTOs de saída
│   ├── exception/        # Exceções de domínio/aplicação
│   └── gateway/          # Interfaces (portas) do domínio
├── inbound/
│   ├── event/            # Listeners de mensageria (RabbitMQ)
│   └── http/             # Controllers REST
└── infrastructure/
    ├── config/           # Configurações técnicas (AMQP, etc.)
    ├── exception/        # Mapeamento de exceções para o framework
    └── repository/       # Implementações dos gateways (JPA)
```

---

## Regras por Camada

### `core/domain/entity/`
- Entidades são classes JPA (`@Entity`, `@Embeddable`).
- **Não importam** nada de `inbound`, `infrastructure`, nem `core.dto`.
- Podem referenciar outras entidades do mesmo pacote e enums de `core.domain.enums`.
- Getters e setters explícitos (sem Lombok por ora).

### `core/domain/enums/`
- Apenas `enum` Java puro, sem dependências de framework.

### `core/domain/event/`
- Eventos são **Java records** imutáveis.
- Sem anotações Spring. Sem dependências de framework.
- Devem conter campos de identificação (`eventId` como `UUID`) e `dataHora` como `LocalDateTime`.
- Convenção de nome: `<Acao>Event.java` (ex: `PagamentoConfirmadoEvent`).

### `core/domain/usecase/`
- Um caso de uso por classe, com **um único método público `execute()`**.
- Anotados com `@Service`.
- Dependem **somente** de interfaces de `core.gateway` e objetos de `core.domain`.
- **Nunca** importam de `inbound`, `infrastructure`, nem de DTOs de `core.dto`.
- Convenção de nome: `<Acao>UC.java` (ex: `BuscarPedidoPorIdUC`, `ListarPedidosUC`).
- Injeção de dependência pelo construtor (sem `@Autowired`).

### `core/dto/`
- DTOs são construídos a partir das entidades via construtor que recebe a entidade.
- **Sem anotações Spring**.
- Convenção de nome: `<Entidade>DTO.java`.
- Podem referenciar entidades de `core.domain.entity` somente no construtor.

### `core/exception/`
- Exceções de domínio estendem `RuntimeException`.
- **Sem dependências de framework** (nem Spring, nem AMQP).
- São mapeadas para exceções de infraestrutura na camada `infrastructure/exception/`.

### `core/gateway/`
- **Interfaces Java puras** que definem o contrato de acesso a dados ou serviços externos.
- **Sem anotações Spring**. Sem referências a JPA, AMQP ou qualquer framework.
- Retornam somente tipos de `core.domain.entity`, primitivos, ou tipos Java padrão (`List`, `Optional`).
- Convenção de nome: `<Entidade>RepositoryGTW.java` ou `<Servico>GTW.java`.
- São implementadas exclusivamente em `infrastructure/repository/` ou equivalente.

---

### `inbound/http/`
- Controllers REST anotados com `@RestController` e `@RequestMapping`.
- **Somente invocam use cases**. Nunca acessam gateways ou repositórios diretamente.
- Conversão de entidade → DTO acontece aqui (usando `new PedidoDTO(pedido)`), não nos use cases.
- Retornam `ResponseEntity<DTO>` ou listas de DTOs.
- **Nunca** importam de `infrastructure`.

### `inbound/event/`
- Listeners de mensageria anotados com `@Component` e `@RabbitListener`.
- **Somente invocam use cases**. Nunca acessam gateways ou repositórios diretamente.
- Referenciam constantes de fila definidas em `infrastructure.config` (ex: `AmqpConfig.PAGAMENTO_CONFIRMADO_QUEUE`).
- **Não fazem tratamento de negócio**: toda lógica fica no use case.

---

### `infrastructure/config/`
- Classes de configuração Spring anotadas com `@Configuration`.
- Definem beans de infraestrutura (filas, exchanges, converters, etc.).
- Constantes de nomes de filas/exchanges são `public static final String` nesta classe.
- **Não contêm lógica de negócio**.

### `infrastructure/exception/`
- Fazem o mapeamento de exceções de `core.exception` para exceções de framework (ex: `AmqpRejectAndDontRequeueException`).
- Isolam o domínio de dependências do framework no tratamento de erros.

### `infrastructure/repository/`
- Interfaces Spring Data JPA que **estendem simultaneamente** `JpaRepository<Entidade, ID>` e a interface gateway correspondente de `core.gateway`.
- Anotadas com `@Repository`.
- Implementam os métodos do gateway usando JPQL (`@Query`) quando necessário.
- **Nunca são injetadas diretamente** em use cases ou inbounds — o contrato usado é sempre a interface gateway.

---

## Regras de Dependência (Lei de Dependência)

```
inbound → core ← infrastructure
```

- `inbound` depende de `core` (use cases, DTOs, eventos, entidades).
- `infrastructure` depende de `core` (implementa gateways, usa entidades).
- `core` **não depende** de `inbound` nem de `infrastructure`.
- **Nenhuma camada** importa diretamente de outra no sentido proibido.

### Imports proibidos:
| Pacote origem       | Nunca importa de            |
|---------------------|-----------------------------|
| `core.domain.*`     | `inbound.*`, `infrastructure.*`, `core.dto` |
| `core.usecase`      | `inbound.*`, `infrastructure.*`, `core.dto` |
| `core.gateway`      | `inbound.*`, `infrastructure.*`             |
| `inbound.*`         | `infrastructure.*` (exceto constantes de config) |

---

## Convenções de Nomenclatura

| Tipo              | Sufixo / Padrão           | Exemplo                          |
|-------------------|---------------------------|----------------------------------|
| Caso de uso       | `UC`                      | `ListarPedidosUC`                |
| Gateway           | `GTW`                     | `PedidoRepositoryGTW`            |
| DTO               | `DTO`                     | `PedidoDTO`                      |
| Evento de domínio | `Event`                   | `PagamentoConfirmadoEvent`       |
| Exceção de domínio| `Exception` ou handler    | `EventExceptionHandler`          |
| Listener de evento| `Listener`                | `PagamentoConfirmadoListener`    |
| Controller REST   | `Controller`              | `PedidoController`               |
| Config infra      | `Config`                  | `AmqpConfig`                     |
| Repository JPA    | `Repository`              | `PedidoRepository`               |

---

## Padrões Gerais

- **Injeção pelo construtor** em todos os componentes Spring. Nunca usar `@Autowired` em campos.
- **Sem Lombok** no projeto atual. Getters e setters são explícitos.
- Use cases não retornam DTOs — retornam entidades de domínio ou tipos padrão Java.
- A conversão entidade → DTO é responsabilidade da camada `inbound`.
- Eventos de domínio são **imutáveis** (records Java).
- Novos módulos devem replicar exatamente esta estrutura de pacotes.
