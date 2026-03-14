package mx.florinda.eats.pedidos.core.domain.usecase;

import jakarta.transaction.Transactional;
import mx.florinda.eats.pedidos.core.domain.entity.Pedido;
import mx.florinda.eats.pedidos.core.gateway.PedidoRepositoryGTW;
import org.springframework.stereotype.Service;

@Service
public class SalvaXmlNotaFiscalPedidoUC {

    private final PedidoRepositoryGTW pedidoRepositoryGTW;

    public SalvaXmlNotaFiscalPedidoUC(PedidoRepositoryGTW pedidoRepositoryGTW) {
        this.pedidoRepositoryGTW = pedidoRepositoryGTW;
    }

    @Transactional
    public void execute(Long id, String xmlNotaFiscal) {
        Pedido pedido = pedidoRepositoryGTW.findById(id).orElseThrow();
        pedido.setXmlNotaFiscal(xmlNotaFiscal);
        pedidoRepositoryGTW.save(pedido);
    }
}
