package mx.florinda.eats.pedidos.core.domain.usecase;

import mx.florinda.eats.pedidos.core.domain.entity.Pedido;
import mx.florinda.eats.pedidos.core.gateway.PedidoRepositoryGTW;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuscarPedidoPorIdUC {

    private final PedidoRepositoryGTW pedidoRepositoryGTW;

    public BuscarPedidoPorIdUC(PedidoRepositoryGTW pedidoRepositoryGTW) {
        this.pedidoRepositoryGTW = pedidoRepositoryGTW;
    }

    public Optional<Pedido> execute(Long id) {
        return pedidoRepositoryGTW.findById(id);
    }

}
