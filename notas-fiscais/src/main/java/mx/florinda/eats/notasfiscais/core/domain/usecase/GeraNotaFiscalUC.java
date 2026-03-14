package mx.florinda.eats.notasfiscais.core.domain.usecase;

import mx.florinda.eats.notasfiscais.core.domain.entity.Pedido;
import mx.florinda.eats.notasfiscais.core.exception.EventExceptionHandler;
import mx.florinda.eats.notasfiscais.core.gateway.PedidoClientGTW;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.logging.Logger;

@Service
public class GeraNotaFiscalUC {

    private final PedidoClientGTW pedidoClientGTW;
    private static final Logger log = Logger.getLogger(GeraNotaFiscalUC.class.getName());

    public GeraNotaFiscalUC(PedidoClientGTW pedidoClientGTW) {
        this.pedidoClientGTW = pedidoClientGTW;
    }

    public String execute(Long pedidoId, BigDecimal valor) {
        log.info("Gerando nota fiscal para pedido: " + pedidoId);
        Pedido pedido = pedidoClientGTW.getById(pedidoId)
                .orElseThrow(() -> new EventExceptionHandler("Pedido não encontrado: " + pedidoId));

        String xml = """
                <xml>
                  <valor>%s</valor>
                  <cliente>
                    <nome>%s</nome>
                    <cpf>%s</cpf>
                    <celular>%s</celular>
                    <endereco>%s</endereco>
                  </cliente>
                </xml>
                """.formatted(valor,
                pedido.getCliente().getNome(),
                pedido.getCliente().getCpf(),
                pedido.getCliente().getCelular(),
                pedido.getCliente().getEndereco());

        pedidoClientGTW.salvaXmlNotaFiscal(pedidoId, xml);

        return xml;
    }
}
