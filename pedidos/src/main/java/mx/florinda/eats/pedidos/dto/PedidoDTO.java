package mx.florinda.eats.pedidos.dto;

import mx.florinda.eats.pedidos.model.Pedido;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoDTO {

    private Long id;
    private LocalDateTime dataHora;
    private String status;
    private ClienteDTO cliente;
    private List<ItemPedidoDTO> itensPedido;

    public PedidoDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.dataHora = pedido.getDataHora();
        this.status = pedido.getStatus().name();
        this.cliente = new ClienteDTO(pedido.getCliente());
        this.itensPedido = pedido.getItensPedido().stream().map(ItemPedidoDTO::new).toList();
    }

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

    public List<ItemPedidoDTO> getItensPedido() {
        return itensPedido;
    }

    public void setItensPedido(List<ItemPedidoDTO> itensPedido) {
        this.itensPedido = itensPedido;
    }
}