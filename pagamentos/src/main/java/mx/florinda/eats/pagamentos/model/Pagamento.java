package mx.florinda.eats.pagamentos.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Pagamento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  public BigDecimal valor;

  @Enumerated(EnumType.STRING)
  public StatusPagamento status;

  public Long pedidoId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }

  public StatusPagamento getStatus() {
    return status;
  }

  public void setStatus(StatusPagamento status) {
    this.status = status;
  }

  public Long getPedidoId() {
    return pedidoId;
  }

  public void setPedidoId(Long pedidoId) {
    this.pedidoId = pedidoId;
  }

  public void confirma() {
    if (!StatusPagamento.CRIADO.equals(this.status)) {
      throw new IllegalStateException("Confirmação de pagamento de id %d negada: status inválido".formatted(this.id));
    }
    this.status = StatusPagamento.CONFIRMADO;
  }
}
