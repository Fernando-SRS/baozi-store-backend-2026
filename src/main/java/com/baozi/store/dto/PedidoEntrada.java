package com.baozi.store.dto;
import jakarta.validation.constraints.*;
public record PedidoEntrada(@NotNull @Positive Long clienteId,
                            @NotNull @Positive Long produtoId,
                            @NotNull @Min(1) Integer quantidade) { }
