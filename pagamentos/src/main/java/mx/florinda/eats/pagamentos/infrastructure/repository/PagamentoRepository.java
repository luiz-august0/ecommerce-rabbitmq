package mx.florinda.eats.pagamentos.infrastructure.repository;

import mx.florinda.eats.pagamentos.core.domain.entity.Pagamento;
import mx.florinda.eats.pagamentos.core.gateway.PagamentoRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long>, PagamentoRepositoryGTW {
}
