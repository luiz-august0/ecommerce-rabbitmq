package mx.florinda.eats.pedidos.core.domain.usecase;

import jakarta.transaction.Transactional;
import mx.florinda.eats.pedidos.core.domain.entity.Pedido;
import mx.florinda.eats.pedidos.core.domain.enums.StatusPedido;
import mx.florinda.eats.pedidos.core.domain.event.PagamentoConfirmadoEvent;
import mx.florinda.eats.pedidos.core.exception.EventExceptionHandler;
import mx.florinda.eats.pedidos.core.gateway.PedidoRepositoryGTW;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class ConfirmaPagamentoPedidoUC {

    private final PedidoRepositoryGTW pedidoRepositoryGTW;
    private static final Logger log = Logger.getLogger(ConfirmaPagamentoPedidoUC.class.getName());

    public ConfirmaPagamentoPedidoUC(PedidoRepositoryGTW pedidoRepositoryGTW) {
        this.pedidoRepositoryGTW = pedidoRepositoryGTW;
    }

    @Transactional
    public void execute(PagamentoConfirmadoEvent pagamentoConfirmadoEvent) {
        log.info("[Pedidos] Recebido novo pagamento confirmado: " + pagamentoConfirmadoEvent);

        try {
            simulaErro(pagamentoConfirmadoEvent);
        } catch (Exception exception) {
            throw new EventExceptionHandler("Rejeitando evento: " + pagamentoConfirmadoEvent);
        }

        Pedido pedido = pedidoRepositoryGTW.findById(pagamentoConfirmadoEvent.pedidoId()).orElseThrow();
        pedido.setStatus(StatusPedido.PAGO);
    }

    private void simulaErro(PagamentoConfirmadoEvent pagamentoConfirmadoEvent) {
        if (pagamentoConfirmadoEvent.pedidoId() % 2 == 0) {
            throw new IllegalStateException("Simulando erro para o evento: " + pagamentoConfirmadoEvent);
        }
    }

}
