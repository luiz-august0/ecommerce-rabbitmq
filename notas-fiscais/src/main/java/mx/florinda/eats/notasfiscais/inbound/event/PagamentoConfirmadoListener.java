package mx.florinda.eats.notasfiscais.inbound.event;

import mx.florinda.eats.notasfiscais.core.domain.event.PagamentoConfirmadoEvent;
import mx.florinda.eats.notasfiscais.core.domain.usecase.GeraNotaFiscalUC;
import mx.florinda.eats.notasfiscais.infrastructure.config.AmqpConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PagamentoConfirmadoListener {

    private final GeraNotaFiscalUC geraNotaFiscalUC;

    public PagamentoConfirmadoListener(GeraNotaFiscalUC geraNotaFiscalUC) {
        this.geraNotaFiscalUC = geraNotaFiscalUC;
    }

    @RabbitListener(queues = AmqpConfig.PAGAMENTO_CONFIRMADO_QUEUE)
    public void pagamentoConfirmado(PagamentoConfirmadoEvent pagamentoConfirmadoEvent) {
        geraNotaFiscalUC.execute(pagamentoConfirmadoEvent.pedidoId(), pagamentoConfirmadoEvent.valor());
    }
}
