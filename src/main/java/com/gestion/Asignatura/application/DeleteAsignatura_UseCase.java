package com.gestion.Asignatura.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.Asignatura.domain.Asignatura_Repository;

public class DeleteAsignatura_UseCase {
    private final Asignatura_Repository repository;

    public DeleteAsignatura_UseCase(Asignatura_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Void> execute(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("El ID de la asignatura debe ser válido");
            }
            return id;
        })
        .thenCompose(validId -> 
            // Primero verificar que existe
            repository.getById(validId)
                .thenCompose(asignatura -> {
                    if (asignatura == null) {
                        throw new RuntimeException("Asignatura no encontrada con ID: " + validId);
                    }
                    // Luego eliminar
                    return repository.delete(validId);
                })
        );
    }
}