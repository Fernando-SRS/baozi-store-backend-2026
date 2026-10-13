package com.baozi.store.controller;
import com.baozi.store.model.Cliente;
import com.baozi.store.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteRepository repository;
    public ClienteController(ClienteRepository repository) { this.repository = repository; }
    @PostMapping
    public ResponseEntity<Cliente> criar(@Valid @RequestBody Cliente entrada) {
        entrada.setId(null);
        Cliente salvo = repository.save(entrada);
        return ResponseEntity.created(URI.create("/clientes/" + salvo.getId())).body(salvo);
    }
    @GetMapping
    public List<Cliente> listar() { return repository.findAll(); }
    @GetMapping("/{id}")
    public Cliente buscar(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        repository.delete(buscar(id));
        repository.flush();
        return ResponseEntity.noContent().build();
    }
}
