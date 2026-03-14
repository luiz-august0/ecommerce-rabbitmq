package mx.florinda.eats.pagamentos.core.domain.usecase;

import mx.florinda.eats.pagamentos.core.domain.entity.Pagamento;
import mx.florinda.eats.pagamentos.core.gateway.PagamentoRepositoryGTW;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuscarPagamentoPorIdUC {

    private final PagamentoRepositoryGTW pagamentoRepositoryGTW;

    public BuscarPagamentoPorIdUC(PagamentoRepositoryGTW pagamentoRepositoryGTW) {
        this.pagamentoRepositoryGTW = pagamentoRepositoryGTW;
    }

    public Optional<Pagamento> execute(Long id) {
        return pagamentoRepositoryGTW.findById(id);
    }
}
