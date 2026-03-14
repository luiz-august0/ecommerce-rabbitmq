package mx.florinda.eats.pagamentos.core.domain.usecase;

import mx.florinda.eats.pagamentos.core.domain.entity.Pagamento;
import mx.florinda.eats.pagamentos.core.gateway.PagamentoRepositoryGTW;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarPagamentosUC {

    private final PagamentoRepositoryGTW pagamentoRepositoryGTW;

    public ListarPagamentosUC(PagamentoRepositoryGTW pagamentoRepositoryGTW) {
        this.pagamentoRepositoryGTW = pagamentoRepositoryGTW;
    }

    public List<Pagamento> execute() {
        return pagamentoRepositoryGTW.findAll();
    }
}
