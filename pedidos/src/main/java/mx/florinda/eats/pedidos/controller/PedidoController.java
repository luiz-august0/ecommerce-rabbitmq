package mx.florinda.eats.pedidos.controller;

import mx.florinda.eats.pedidos.dto.PedidoDTO;
import mx.florinda.eats.pedidos.model.Pedido;
import mx.florinda.eats.pedidos.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

  private final PedidoRepository pedidoRepository;

  public PedidoController(PedidoRepository pedidoRepository) {
    this.pedidoRepository = pedidoRepository;
  }

  @GetMapping
  public List<PedidoDTO> lista() {
    List<Pedido> pedidos = pedidoRepository.listaComItens();
    return pedidos.stream().map(PedidoDTO::new).toList();
  }

  @GetMapping("/{id}")
  public ResponseEntity<PedidoDTO> porId(@PathVariable("id") Long id) {
    return pedidoRepository.findById(id)
            .map(pedido -> ResponseEntity.ok(new PedidoDTO(pedido)))
            .orElse(ResponseEntity.notFound().build());
  }

}
