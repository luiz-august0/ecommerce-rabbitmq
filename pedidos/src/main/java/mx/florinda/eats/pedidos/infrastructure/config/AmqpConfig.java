package mx.florinda.eats.pedidos.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    public static final String PAGAMENTO_CONFIRMADO_QUEUE = "pedidos.pagamento-confirmado";
    public static final String PAGAMENTO_CONFIRMADO_QUEUE_DLQ = PAGAMENTO_CONFIRMADO_QUEUE + ".dlq";
    public static final String PEDIDOS_DLX = "pedidos.dlx";

    @Bean
    public DirectExchange pedidosDLX() {
        return new DirectExchange(PEDIDOS_DLX);
    }

    @Bean
    public Queue pedidosPagamentosConfirmadoDLQ() {
        return new Queue(PAGAMENTO_CONFIRMADO_QUEUE_DLQ);
    }

    @Bean
    public Binding pedidosPagamentosConfirmadoDLQBinding(Queue pedidosPagamentosConfirmadoDLQ, DirectExchange pedidosDLX) {
        return BindingBuilder.bind(pedidosPagamentosConfirmadoDLQ)
                .to(pedidosDLX)
                .with(PAGAMENTO_CONFIRMADO_QUEUE_DLQ);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public TopicExchange pagamentosExchange() {
        return new TopicExchange("pagamentos");
    }

    @Bean
    public Queue filaPagamentosConfirmadosPedido() {
        return QueueBuilder.durable(PAGAMENTO_CONFIRMADO_QUEUE)
                .deadLetterExchange(PEDIDOS_DLX)
                .deadLetterRoutingKey(PAGAMENTO_CONFIRMADO_QUEUE_DLQ)
                .build();
    }

    @Bean
    public Binding bindingPagamentosConfirmados(Queue filaPagamentosConfirmadosPedido,
                                                TopicExchange pagamentosExchange) {
        return BindingBuilder.bind(filaPagamentosConfirmadosPedido)
                .to(pagamentosExchange)
                .with("pagamentos.pagamento.confirmado");
    }

}
