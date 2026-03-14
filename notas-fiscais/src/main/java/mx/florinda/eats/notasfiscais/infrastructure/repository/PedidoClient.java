package mx.florinda.eats.notasfiscais.infrastructure.repository;

import mx.florinda.eats.notasfiscais.core.domain.entity.Cliente;
import mx.florinda.eats.notasfiscais.core.domain.entity.Pedido;
import mx.florinda.eats.notasfiscais.core.gateway.PedidoClientGTW;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class PedidoClient implements PedidoClientGTW {

    private final String pedidoBaseUrl;

    public PedidoClient(@Value("${pedido.api.base-url}") String pedidoBaseUrl) {
        this.pedidoBaseUrl = pedidoBaseUrl;
    }

    @Override
    public Optional<Pedido> getById(Long pedidoId) {
        ResponseEntity<PedidoResponse> responseEntity = RestClient.create()
                .get()
                .uri(pedidoBaseUrl + "/pedidos/{id}", pedidoId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(PedidoResponse.class);

        if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Optional.empty();
        }

        return Optional.ofNullable(responseEntity.getBody()).map(this::mapToDomain);
    }

    @Override
    public void salvaXmlNotaFiscal(Long pedidoId, String xmlNotaFiscal) {
        RestClient.create()
                .patch()
                .uri(pedidoBaseUrl + "/pedidos/{id}/nota-fiscal", pedidoId)
                .contentType(MediaType.TEXT_PLAIN)
                .body(xmlNotaFiscal)
                .retrieve()
                .toBodilessEntity();
    }

    private Pedido mapToDomain(PedidoResponse response) {
        Cliente cliente = new Cliente();
        cliente.setNome(response.cliente().nome());
        cliente.setCpf(response.cliente().cpf());
        cliente.setCelular(response.cliente().celular());
        cliente.setEndereco(response.cliente().endereco());

        Pedido pedido = new Pedido();
        pedido.setId(response.id());
        pedido.setDataHora(response.dataHora());
        pedido.setStatus(response.status());
        pedido.setCliente(cliente);
        return pedido;
    }

    private record PedidoResponse(Long id, LocalDateTime dataHora, String status, ClienteResponse cliente) {}

    private record ClienteResponse(String nome, String cpf, String celular, String endereco) {}
}
