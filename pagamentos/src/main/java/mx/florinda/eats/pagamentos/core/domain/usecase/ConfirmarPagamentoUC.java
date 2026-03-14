package mx.florinda.eats.pagamentos.core.domain.usecase;

import jakarta.transaction.Transactional;
import mx.florinda.eats.pagamentos.core.domain.entity.Pagamento;
import mx.florinda.eats.pagamentos.core.domain.event.PagamentoConfirmadoEvent;
import mx.florinda.eats.pagamentos.core.gateway.PagamentoEventPublisherGTW;
import mx.florinda.eats.pagamentos.core.gateway.PagamentoRepositoryGTW;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfirmarPagamentoUC {

    private final PagamentoRepositoryGTW pagamentoRepositoryGTW;
    private final PagamentoEventPublisherGTW pagamentoEventPublisherGTW;

    public ConfirmarPagamentoUC(PagamentoRepositoryGTW pagamentoRepositoryGTW,
                                PagamentoEventPublisherGTW pagamentoEventPublisherGTW) {
        this.pagamentoRepositoryGTW = pagamentoRepositoryGTW;
        this.pagamentoEventPublisherGTW = pagamentoEventPublisherGTW;
    }

    @Transactional
    public Optional<Pagamento> execute(Long id) {
        return pagamentoRepositoryGTW.findById(id)
                .map(pagamento -> {
                    pagamento.confirma();
                    pagamentoRepositoryGTW.save(pagamento);
                    pagamentoEventPublisherGTW.publish(
                            new PagamentoConfirmadoEvent(pagamento.getId(), pagamento.getValor(), pagamento.getPedidoId())
                    );
                    return pagamento;
                });
    }
}
