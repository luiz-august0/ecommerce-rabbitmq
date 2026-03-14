package mx.florinda.eats.notasfiscais.integration.rabbit;

import mx.florinda.eats.notasfiscais.config.AmqpConfig;
import mx.florinda.eats.notasfiscais.service.NotaFiscalService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PagamentoConfirmadoListener {

  private final NotaFiscalService notaFiscalService;
  private final long delayEmMS;

  public PagamentoConfirmadoListener(NotaFiscalService notaFiscalService, @Value("${app.simulacao.delay:0}") long delayEmMS) {
    this.notaFiscalService = notaFiscalService;
    this.delayEmMS = delayEmMS;
  }

  @RabbitListener(queues = AmqpConfig.PAGAMENTO_CONFIRMADO_QUEUE)
  public void pagamentoConfirmado(PagamentoConfirmadoEvent pagamentoConfirmadoEvent) {

    System.out.println("[Notas Fiscais] Recebido novo pagamento confirmado: " + pagamentoConfirmadoEvent);

    if (delayEmMS > 0) {
      System.out.println("Simulando delay (ms): " + delayEmMS);
      try {
        Thread.sleep(delayEmMS);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.out.println("Thread interrompida");
      }
    }

    Long pedidoId = pagamentoConfirmadoEvent.pedidoId();
    BigDecimal valorPagamento = pagamentoConfirmadoEvent.valor();
    String notaFiscalXML = notaFiscalService.geraNotaFiscal(pedidoId, valorPagamento);
    System.out.println(notaFiscalXML);

  }

}
