package mx.florinda.eats.pedidos.inbound.http;

import mx.florinda.eats.pedidos.core.domain.usecase.BuscarPedidoPorIdUC;
import mx.florinda.eats.pedidos.core.domain.usecase.ListarPedidosUC;
import mx.florinda.eats.pedidos.core.domain.usecase.SalvaXmlNotaFiscalPedidoUC;
import mx.florinda.eats.pedidos.core.dto.PedidoDTO;
import mx.florinda.eats.pedidos.core.domain.entity.Pedido;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

  private final ListarPedidosUC listarPedidosUC;
  private final BuscarPedidoPorIdUC buscarPedidoPorIdUC;
  private final SalvaXmlNotaFiscalPedidoUC salvaXmlNotaFiscalPedidoUC;

  public PedidoController(ListarPedidosUC listarPedidosUC, BuscarPedidoPorIdUC buscarPedidoPorIdUC, SalvaXmlNotaFiscalPedidoUC salvaXmlNotaFiscalPedidoUC) {
    this.listarPedidosUC = listarPedidosUC;
    this.buscarPedidoPorIdUC = buscarPedidoPorIdUC;
    this.salvaXmlNotaFiscalPedidoUC = salvaXmlNotaFiscalPedidoUC;
  }

  @GetMapping
  public List<PedidoDTO> lista() {
    List<Pedido> pedidos = listarPedidosUC.execute();
    return pedidos.stream().map(PedidoDTO::new).toList();
  }

  @GetMapping("/{id}")
  public ResponseEntity<PedidoDTO> porId(@PathVariable("id") Long id) {
    return buscarPedidoPorIdUC.execute(id)
            .map(pedido -> ResponseEntity.ok(new PedidoDTO(pedido)))
            .orElse(ResponseEntity.notFound().build());
  }

  @PatchMapping("/{id}/nota-fiscal")
  public ResponseEntity<Void> salvaXmlNotaFiscal(@PathVariable("id") Long id, @RequestBody String xmlNotaFiscal) {
    salvaXmlNotaFiscalPedidoUC.execute(id, xmlNotaFiscal);
    return ResponseEntity.noContent().build();
  }

}
