package com.baozi.store.dto;
import com.baozi.store.model.Pedido;
public record PedidoSaida(Long id, Long clienteId, Long produtoId, Integer quantidade) {
    public static PedidoSaida de(Pedido pedido) {
        return new PedidoSaida(pedido.getId(), pedido.getCliente().getId(),
            pedido.getProduto().getId(), pedido.getQuantidade());
    }
}
