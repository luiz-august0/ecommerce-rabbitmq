package mx.florinda.eats.notasfiscais.inbound.http;

import mx.florinda.eats.notasfiscais.core.domain.usecase.GeraNotaFiscalUC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/notas-fiscais")
public class NotaFiscalController {

    private final GeraNotaFiscalUC geraNotaFiscalUC;

    public NotaFiscalController(GeraNotaFiscalUC geraNotaFiscalUC) {
        this.geraNotaFiscalUC = geraNotaFiscalUC;
    }

    @GetMapping("/pedidos/{pedidoId}")
    public ResponseEntity<String> notaDoPedidoId(@PathVariable("pedidoId") Long pedidoId,
                                                  @RequestParam(value = "valor", defaultValue = "0.0") BigDecimal valor) {
        return ResponseEntity.ok(geraNotaFiscalUC.execute(pedidoId, valor));
    }
}
