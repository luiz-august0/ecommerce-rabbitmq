package mx.florinda.eats.notasfiscais.service;

import mx.florinda.eats.notasfiscais.repository.PedidoClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class NotaFiscalService {

  private final PedidoClient pedidoClient;

  public NotaFiscalService(PedidoClient pedidoClient) {
    this.pedidoClient = pedidoClient;
  }

  public String geraNotaFiscal(Long pedidoId, BigDecimal valor) {

    return pedidoClient.getById(pedidoId)
        .map(pedidoDTO -> """
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
            pedidoDTO.getCliente().getNome(),
            pedidoDTO.getCliente().getCpf(),
            pedidoDTO.getCliente().getCelular(),
            pedidoDTO.getCliente().getEndereco()))
        .orElseThrow();
  }

}
