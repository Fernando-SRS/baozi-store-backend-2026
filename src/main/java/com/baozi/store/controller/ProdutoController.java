package com.baozi.store.controller;
import com.baozi.store.model.Produto;
import com.baozi.store.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    private final ProdutoRepository repository;
    public ProdutoController(ProdutoRepository repository) { this.repository = repository; }
    @PostMapping
    public ResponseEntity<Produto> criar(@Valid @RequestBody Produto entrada) {
        entrada.setId(null);
        Produto salvo = repository.save(entrada);
        return ResponseEntity.created(URI.create("/produtos/" + salvo.getId())).body(salvo);
    }
    @GetMapping
    public List<Produto> listar() { return repository.findAll(); }
    @GetMapping("/{id}")
    public Produto buscar(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        repository.delete(buscar(id));
        repository.flush();
        return ResponseEntity.noContent().build();
    }
}
