package mx.florinda.eats.pedidos.core.domain.entity;

import jakarta.persistence.*;
import mx.florinda.eats.pedidos.core.domain.enums.StatusPedido;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @Embedded
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itensPedido;

    @Column(name = "xml_nota_fiscal", columnDefinition = "TEXT")
    private String xmlNotaFiscal;

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

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemPedido> getItensPedido() {
        return itensPedido;
    }

    public void setItensPedido(List<ItemPedido> itensPedido) {
        this.itensPedido = itensPedido;
    }

    public String getXmlNotaFiscal() {
        return xmlNotaFiscal;
    }

    public void setXmlNotaFiscal(String xmlNotaFiscal) {
        this.xmlNotaFiscal = xmlNotaFiscal;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", dataHora=" + dataHora +
                ", status=" + status +
                ", cliente=" + cliente +
                ", itensPedido=" + itensPedido +
                '}';
    }
}
