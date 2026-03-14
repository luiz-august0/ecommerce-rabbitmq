package mx.florinda.eats.pagamentos.infrastructure.amqp;

import mx.florinda.eats.pagamentos.core.domain.event.PagamentoConfirmadoEvent;
import mx.florinda.eats.pagamentos.core.gateway.PagamentoEventPublisherGTW;
import mx.florinda.eats.pagamentos.infrastructure.config.AmqpConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class PagamentoEventPublisher implements PagamentoEventPublisherGTW {

    private final AmqpTemplate amqpTemplate;

    public PagamentoEventPublisher(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @Override
    public void publish(PagamentoConfirmadoEvent event) {
        amqpTemplate.convertAndSend(AmqpConfig.PAGAMENTOS_EXCHANGE, AmqpConfig.PAGAMENTO_CONFIRMADO_ROUTING_KEY, event);
    }
}
