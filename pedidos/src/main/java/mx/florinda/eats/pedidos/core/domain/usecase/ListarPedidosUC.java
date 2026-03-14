package mx.florinda.eats.pedidos.core.domain.usecase;

import mx.florinda.eats.pedidos.core.domain.entity.Pedido;
import mx.florinda.eats.pedidos.core.dto.PedidoDTO;
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