package mx.florinda.eats.notasfiscais.infrastructure.exception;

import mx.florinda.eats.notasfiscais.core.exception.EventExceptionHandler;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(EventExceptionHandler.class)
    public void handle(EventExceptionHandler eventExceptionHandler) {
        throw new AmqpRejectAndDontRequeueException(eventExceptionHandler.getMessage());
    }
}
