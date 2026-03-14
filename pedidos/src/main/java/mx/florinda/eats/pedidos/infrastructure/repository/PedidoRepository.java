package mx.florinda.eats.pedidos.infrastructure.repository;

import mx.florinda.eats.pedidos.core.domain.entity.Pedido;
import mx.florinda.eats.pedidos.core.gateway.PedidoRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>, PedidoRepositoryGTW {

    @Query("select p from Pedido p join fetch p.itensPedido i join fetch i.itemCardapio ic order by p.id")
    List<Pedido> listaComItens();

}
