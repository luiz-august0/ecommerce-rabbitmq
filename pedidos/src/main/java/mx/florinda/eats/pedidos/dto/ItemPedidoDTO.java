package mx.florinda.eats.pedidos.dto;

import mx.florinda.eats.pedidos.model.ItemPedido;

import java.math.BigDecimal;

public class ItemPedidoDTO {

    private Long quantidade;
    private BigDecimal precoUnitario;
    private String observacao;
    private ItemCardapioDTO itemCardapio;

    ItemPedidoDTO(ItemPedido itemPedido) {
        this.quantidade = itemPedido.getQuantidade();
        this.precoUnitario = itemPedido.getPrecoUnitario();
        this.observacao = itemPedido.getObservacao();
        this.itemCardapio = new ItemCardapioDTO(itemPedido.getItemCardapio());
    }


    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public ItemCardapioDTO getItemCardapio() {
        return itemCardapio;
    }

    public void setItemCardapio(ItemCardapioDTO itemCardapio) {
        this.itemCardapio = itemCardapio;
    }
}