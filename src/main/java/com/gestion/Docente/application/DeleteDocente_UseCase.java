package com.gestion.Docente.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.Docente.domain.Docente_Repository;

public class DeleteDocente_UseCase {
    private final Docente_Repository repository;

    public DeleteDocente_UseCase(Docente_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Void> execute(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("El ID del docente debe ser válido");
            }
            return id;
        })
        .thenCompose(validId -> 
            // Primero verificar que existe
            repository.getById(validId)
                .thenCompose(docente -> {
                    if (docente == null) {
                        throw new RuntimeException("Docente no encontrado con ID: " + validId);
                    }
                    // Luego eliminar
                    return repository.delete(validId);
                })
        );
    }
}