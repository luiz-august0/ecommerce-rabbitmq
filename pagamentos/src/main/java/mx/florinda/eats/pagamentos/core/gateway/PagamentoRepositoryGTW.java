package mx.florinda.eats.pagamentos.core.gateway;

import mx.florinda.eats.pagamentos.core.domain.entity.Pagamento;

import java.util.List;
import java.util.Optional;

public interface PagamentoRepositoryGTW {
    List<Pagamento> findAll();
    Optional<Pagamento> findById(Long id);
    Pagamento save(Pagamento pagamento);
}
