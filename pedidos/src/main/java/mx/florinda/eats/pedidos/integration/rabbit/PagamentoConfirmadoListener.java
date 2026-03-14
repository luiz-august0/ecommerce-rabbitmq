package mx.florinda.eats.pedidos.integration.rabbit;

import jakarta.transaction.Transactional;
import mx.florinda.eats.pedidos.config.AmqpConfig;
import mx.florinda.eats.pedidos.model.Pedido;
import mx.florinda.eats.pedidos.model.StatusPedido;
import mx.florinda.eats.pedidos.repository.PedidoRepository;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PagamentoConfirmadoListener {

  private final PedidoRepository pedidoRepository;

  public PagamentoConfirmadoListener(PedidoRepository pedidoRepository) {
    this.pedidoRepository = pedidoRepository;
  }

  @RabbitListener(queues = AmqpConfig.PAGAMENTO_CONFIRMADO_QUEUE)
  @Transactional
  public void pagamentoConfirmado(PagamentoConfirmadoEvent pagamentoConfirmadoEvent) {

    System.out.println("[Pedidos] Recebido novo pagamento confirmado: " + pagamentoConfirmadoEvent);

    try {
      simulaErro(pagamentoConfirmadoEvent);
    } catch (Exception exception) {
      throw new AmqpRejectAndDontRequeueException("Rejeitando evento: " + pagamentoConfirmadoEvent);
    }

    Pedido pedido = pedidoRepository.findById(pagamentoConfirmadoEvent.pedidoId())
        .orElseThrow();
    pedido.setStatus(StatusPedido.PAGO);
  }

  private void simulaErro(PagamentoConfirmadoEvent pagamentoConfirmadoEvent) {
    if (pagamentoConfirmadoEvent.pedidoId() % 2 == 0) {
      throw new IllegalStateException("Simulando erro para o evento: " + pagamentoConfirmadoEvent);
    }
  }

}
