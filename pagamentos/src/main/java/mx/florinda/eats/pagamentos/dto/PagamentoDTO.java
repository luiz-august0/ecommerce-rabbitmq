package mx.florinda.eats.pagamentos.dto;

import mx.florinda.eats.pagamentos.model.Pagamento;

import java.math.BigDecimal;

public class PagamentoDTO {

  private Long id;
  private String status;
  private BigDecimal valor;
  private Long pedidoId;

  public PagamentoDTO(Pagamento pagamento) {
    this.id = pagamento.getId();
    this.status = pagamento.getStatus().name();
    this.valor =  pagamento.getValor();
    this.pedidoId = pagamento.getPedidoId();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }

  public Long getPedidoId() {
    return pedidoId;
  }

  public void setPedidoId(Long pedidoId) {
    this.pedidoId = pedidoId;
  }
}
