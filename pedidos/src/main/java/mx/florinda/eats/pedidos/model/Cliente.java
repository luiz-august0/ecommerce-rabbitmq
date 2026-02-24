package mx.florinda.eats.pedidos.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Cliente {

    @Column(name = "nome_cliente")
    private String nome;

    @Column(name = "cpf_cliente")
    private String cpf;

    @Column(name = "celular_cliente")
    private String celular;

    @Column(name = "endereco_cliente")
    private String endereco;

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

  @Override
  public String toString() {
    return "Cliente{" +
        "nome='" + nome + '\'' +
        ", cpf='" + cpf + '\'' +
        ", celular='" + celular + '\'' +
        ", endereco='" + endereco + '\'' +
        '}';
  }
}
