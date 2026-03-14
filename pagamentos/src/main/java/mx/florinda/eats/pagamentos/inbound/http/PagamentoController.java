package mx.florinda.eats.pagamentos.inbound.http;

import mx.florinda.eats.pagamentos.core.domain.usecase.BuscarPagamentoPorIdUC;
import mx.florinda.eats.pagamentos.core.domain.usecase.ConfirmarPagamentoUC;
import mx.florinda.eats.pagamentos.core.domain.usecase.ListarPagamentosUC;
import mx.florinda.eats.pagamentos.core.dto.PagamentoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final ListarPagamentosUC listarPagamentosUC;
    private final BuscarPagamentoPorIdUC buscarPagamentoPorIdUC;
    private final ConfirmarPagamentoUC confirmarPagamentoUC;

    public PagamentoController(ListarPagamentosUC listarPagamentosUC,
                               BuscarPagamentoPorIdUC buscarPagamentoPorIdUC,
                               ConfirmarPagamentoUC confirmarPagamentoUC) {
        this.listarPagamentosUC = listarPagamentosUC;
        this.buscarPagamentoPorIdUC = buscarPagamentoPorIdUC;
        this.confirmarPagamentoUC = confirmarPagamentoUC;
    }

    @GetMapping
    public List<PagamentoDTO> lista() {
        return listarPagamentosUC.execute().stream().map(PagamentoDTO::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDTO> porId(@PathVariable Long id) {
        return buscarPagamentoPorIdUC.execute(id)
                .map(pagamento -> ResponseEntity.ok(new PagamentoDTO(pagamento)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDTO> confirma(@PathVariable Long id) {
        return confirmarPagamentoUC.execute(id)
                .map(pagamento -> ResponseEntity.ok(new PagamentoDTO(pagamento)))
                .orElse(ResponseEntity.notFound().build());
    }
}
