package mx.florinda.eats.pedidos.core.dto;

import mx.florinda.eats.pedidos.core.domain.entity.ItemCardapio;

import java.math.BigDecimal;

public class ItemCardapioDTO {

    private String nome;
    private String descricao;
    private String categoria;
    private BigDecimal preco;
    private BigDecimal precoPromocional;

    public ItemCardapioDTO(ItemCardapio itemCardapio) {
        this.nome = itemCardapio.getNome();
        this.descricao = itemCardapio.getDescricao();
        this.categoria = itemCardapio.getCategoria().name();
        this.preco = itemCardapio.getPreco();
        this.precoPromocional = itemCardapio.getPrecoPromocional();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getPrecoPromocional() {
        return precoPromocional;
    }

    public void setPrecoPromocional(BigDecimal precoPromocional) {
        this.precoPromocional = precoPromocional;
    }
}