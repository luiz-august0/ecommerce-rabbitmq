package mx.florinda.eats.pedidos.core.dto;

import mx.florinda.eats.pedidos.core.domain.entity.Cliente;

public class ClienteDTO {

    private String nome;
    private String cpf;
    private String celular;
    private String endereco;

    public ClienteDTO(Cliente cliente) {
        this.nome = cliente.getNome();
        this.cpf = cliente.getCpf();
        this.celular = cliente.getCelular();
        this.endereco = cliente.getEndereco();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}