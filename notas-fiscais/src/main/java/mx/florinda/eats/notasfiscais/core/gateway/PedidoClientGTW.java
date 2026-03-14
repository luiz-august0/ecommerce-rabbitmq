package mx.florinda.eats.notasfiscais.core.gateway;

import mx.florinda.eats.notasfiscais.core.domain.entity.Pedido;

import java.util.Optional;

public interface PedidoClientGTW {
    Optional<Pedido> getById(Long pedidoId);

    void salvaXmlNotaFiscal(Long pedidoId, String xmlNotaFiscal);
}
