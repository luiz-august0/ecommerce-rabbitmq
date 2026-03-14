package mx.florinda.eats.pagamentos.core.gateway;

import mx.florinda.eats.pagamentos.core.domain.event.PagamentoConfirmadoEvent;

public interface PagamentoEventPublisherGTW {
    void publish(PagamentoConfirmadoEvent event);
}
