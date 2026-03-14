package mx.florinda.eats.pagamentos.infrastructure.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    public static final String PAGAMENTOS_EXCHANGE = "pagamentos";
    public static final String PAGAMENTO_CONFIRMADO_ROUTING_KEY = "pagamentos.pagamento.confirmado";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public TopicExchange pagamentosExchange() {
        return new TopicExchange(PAGAMENTOS_EXCHANGE);
    }
}
