package com.baozi.store.controller;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestControllerAdvice
public class ErroControllerAdvice {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> conflito(DataIntegrityViolationException erro) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro",
            "Registro vinculado a um pedido. Exclua o pedido antes de excluir o cliente ou produto."));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> invalido(MethodArgumentNotValidException erro) {
        var campos = erro.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage()).toList();
        return ResponseEntity.badRequest().body(Map.of("erro", "Dados inválidos", "campos", campos));
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> naoEncontrado(ResponseStatusException erro) {
        return ResponseEntity.status(erro.getStatusCode()).body(Map.of("erro", erro.getReason()));
    }
}
