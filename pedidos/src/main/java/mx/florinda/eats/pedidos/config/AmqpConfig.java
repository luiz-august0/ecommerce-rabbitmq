package mx.florinda.eats.pedidos.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

  public static final String PAGAMENTO_CONFIRMADO_QUEUE = "pedidos.pagamento-confirmado";
  public static final String PAGAMENTO_CONFIRMADO_DLQ = PAGAMENTO_CONFIRMADO_QUEUE + ".dlq";
  public static final String PEDIDOS_DLX = "pedidos.dlx";

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new JacksonJsonMessageConverter();
  }

  @Bean
  public Queue filaPagamentoConfirmado() {
    return QueueBuilder.durable(PAGAMENTO_CONFIRMADO_QUEUE)
        .deadLetterExchange(PEDIDOS_DLX)
        .deadLetterRoutingKey(PAGAMENTO_CONFIRMADO_DLQ)
        .build();
  }

  @Bean
  public TopicExchange pagamentosExchange() {
    return new TopicExchange("pagamentos");
  }

  @Bean
  public Binding bindingPagamentoConfirmado(Queue filaPagamentoConfirmado, TopicExchange pagamentosExchange) {
    return BindingBuilder
        .bind(filaPagamentoConfirmado)
        .to(pagamentosExchange)
        .with("pagamentos.pagamento.confirmado");
  }

  @Bean
  public DirectExchange pedidosDLX() {
    return new DirectExchange(PEDIDOS_DLX);
  }

  @Bean
  public Queue filaPagamentoConfirmadoDLQ() {
    return QueueBuilder.durable(PAGAMENTO_CONFIRMADO_DLQ).build();
  }

  @Bean
  public Binding dlqBinding(Queue filaPagamentoConfirmadoDLQ, DirectExchange pedidosDLX) {
    return BindingBuilder
        .bind(filaPagamentoConfirmadoDLQ)
        .to(pedidosDLX)
        .with(PAGAMENTO_CONFIRMADO_DLQ);
  }

}
