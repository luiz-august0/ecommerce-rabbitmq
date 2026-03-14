package mx.florinda.eats.pagamentos.infrastructure.exception;

import mx.florinda.eats.pagamentos.core.exception.PagamentoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(PagamentoException.class)
    public ResponseEntity<String> handle(PagamentoException pagamentoException) {
        return ResponseEntity.unprocessableEntity().body(pagamentoException.getMessage());
    }
}
