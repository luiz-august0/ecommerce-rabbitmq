package mx.florinda.eats.pedidos.inbound.event;

import mx.florinda.eats.pedidos.core.domain.event.PagamentoConfirmadoEvent;
import mx.florinda.eats.pedidos.core.domain.usecase.ConfirmaPagamentoPedidoUC;
import mx.florinda.eats.pedidos.infrastructure.config.AmqpConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PagamentoConfirmadoListener {

    private final ConfirmaPagamentoPedidoUC confirmaPagamentoPedidoUC;

    public PagamentoConfirmadoListener(ConfirmaPagamentoPedidoUC confirmaPagamentoPedidoUC) {
        this.confirmaPagamentoPedidoUC = confirmaPagamentoPedidoUC;
    }

    @RabbitListener(queues = AmqpConfig.PAGAMENTO_CONFIRMADO_QUEUE)
    public void pagamentoConfirmado(PagamentoConfirmadoEvent pagamentoConfirmadoEvent) {
        confirmaPagamentoPedidoUC.execute(pagamentoConfirmadoEvent);
    }

}
