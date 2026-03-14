package mx.florinda.eats.pedidos.core.gateway;

import mx.florinda.eats.pedidos.core.domain.entity.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoRepositoryGTW {

    List<Pedido> listaComItens();

    Optional<Pedido> findById(Long id);

    Pedido save(Pedido pedido);

}
