package mx.florinda.eats.pedidos.infrastructure.exception;

import mx.florinda.eats.pedidos.core.exception.EventExceptionHandler;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(EventExceptionHandler.class)
    public void handle(EventExceptionHandler eventExceptionHandler) {
        throw new AmqpRejectAndDontRequeueException(eventExceptionHandler.getMessage());
    }

}
