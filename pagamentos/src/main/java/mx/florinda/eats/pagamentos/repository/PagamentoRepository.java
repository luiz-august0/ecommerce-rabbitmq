package mx.florinda.eats.pagamentos.repository;

import mx.florinda.eats.pagamentos.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}
