package mx.florinda.eats.notasfiscais.dto;

import java.time.LocalDateTime;

public class PedidoDTO {

  private Long id;
  private LocalDateTime dataHora;
  private String status;
  private ClienteDTO cliente;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getDataHora() {
    return dataHora;
  }

  public void setDataHora(LocalDateTime dataHora) {
    this.dataHora = dataHora;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public ClienteDTO getCliente() {
    return cliente;
  }

  public void setCliente(ClienteDTO cliente) {
    this.cliente = cliente;
  }
}
