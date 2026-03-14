---
description: Regras para criação e modificação de Use Cases (UC) na arquitetura limpa do projeto
---

# Use Cases

## Localização
`<modulo>/src/main/java/mx/florinda/eats/<modulo>/core/domain/usecase/`

## Estrutura obrigatória
- Uma classe por caso de uso com **um único método público `execute()`**
- Anotada com `@Service`
- Injeção de dependências **exclusivamente pelo construtor** (sem `@Autowired`)
- O construtor recebe somente interfaces de `core.gateway`

## Convenção de nome
`<Acao><Entidade>UC.java`

Exemplos: `ListarPedidosUC`, `BuscarPedidoPorIdUC`, `ConfirmaPagamentoPedidoUC`

## Dependências permitidas
- `core.domain.entity.*`
- `core.domain.enums.*`
- `core.domain.event.*`
- `core.exception.*`
- `core.gateway.*`
- `org.springframework.stereotype.Service`
- `jakarta.transaction.Transactional` (quando necessário)
- Tipos Java padrão (`java.util.*`, `java.time.*`, etc.)

## Dependências proibidas
- `inbound.*` — nunca
- `infrastructure.*` — nunca
- `core.dto.*` — use cases não retornam DTOs
- Qualquer classe concreta de repositório ou framework de persistência

## Retorno
- Use cases retornam **entidades de domínio** ou tipos Java padrão (`List`, `Optional`, `void`)
- **Nunca retornam DTOs** — a conversão é responsabilidade da camada `inbound`

## Exemplo
```java
package mx.florinda.eats.pedidos.core.domain.usecase;

import mx.florinda.eats.pedidos.core.domain.entity.Pedido;
import mx.florinda.eats.pedidos.core.gateway.PedidoRepositoryGTW;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ListarPedidosUC {

    private final PedidoRepositoryGTW pedidoRepositoryGTW;

    public ListarPedidosUC(PedidoRepositoryGTW pedidoRepositoryGTW) {
        this.pedidoRepositoryGTW = pedidoRepositoryGTW;
    }

    public List<Pedido> execute() {
        return pedidoRepositoryGTW.listaComItens();
    }
}
```
