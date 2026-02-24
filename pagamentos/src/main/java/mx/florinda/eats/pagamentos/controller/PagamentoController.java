package mx.florinda.eats.pagamentos.controller;

import jakarta.transaction.Transactional;
import mx.florinda.eats.pagamentos.dto.PagamentoDTO;
import mx.florinda.eats.pagamentos.model.Pagamento;
import mx.florinda.eats.pagamentos.repository.PagamentoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

  private final PagamentoRepository pagamentoRepository;

  public PagamentoController(PagamentoRepository pagamentoRepository) {
    this.pagamentoRepository = pagamentoRepository;
  }

  @GetMapping
  public List<PagamentoDTO> lista() {
    List<Pagamento> pagamentos = pagamentoRepository.findAll();
    return pagamentos.stream().map(PagamentoDTO::new).toList();
  }

  @GetMapping("/{id}")
  public ResponseEntity<PagamentoDTO> porId(@PathVariable("id") Long id) {
    return pagamentoRepository.findById(id)
        .map(pagamento -> ResponseEntity.ok(new PagamentoDTO(pagamento)))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  @Transactional
  public  ResponseEntity<PagamentoDTO> confirma(@PathVariable("id") Long id) {
    return pagamentoRepository.findById(id)
        .map(pagamento -> {
          pagamento.confirma();
          return ResponseEntity.ok(new PagamentoDTO(pagamento));
        })
        .orElse(ResponseEntity.notFound().build());
  }

}
