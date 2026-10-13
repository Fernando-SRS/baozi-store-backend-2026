package com.baozi.store.controller;
import com.baozi.store.dto.*;
import com.baozi.store.model.*;
import com.baozi.store.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoRepository pedidos;
    private final ClienteRepository clientes;
    private final ProdutoRepository produtos;
    public PedidoController(PedidoRepository pedidos, ClienteRepository clientes, ProdutoRepository produtos) {
        this.pedidos = pedidos; this.clientes = clientes; this.produtos = produtos;
    }
    @PostMapping
    public ResponseEntity<PedidoSaida> criar(@Valid @RequestBody PedidoEntrada entrada) {
        Cliente cliente = clientes.findById(entrada.clienteId()).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
        Produto produto = produtos.findById(entrada.produtoId()).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
        if (!Boolean.TRUE.equals(produto.getEstoque())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Produto indisponível em estoque");
        }
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente); pedido.setProduto(produto); pedido.setQuantidade(entrada.quantidade());
        Pedido salvo = pedidos.save(pedido);
        return ResponseEntity.created(URI.create("/pedidos/" + salvo.getId())).body(PedidoSaida.de(salvo));
    }
    @GetMapping
    public List<PedidoSaida> listar() {
        return pedidos.findAll().stream().map(PedidoSaida::de).toList();
    }
    @GetMapping("/{id}")
    public PedidoSaida buscar(@PathVariable Long id) { return PedidoSaida.de(encontrar(id)); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pedidos.delete(encontrar(id));
        return ResponseEntity.noContent().build();
    }
    private Pedido encontrar(Long id) {
        return pedidos.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
    }
}
