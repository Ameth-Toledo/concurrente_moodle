package com.gestion.Grupo.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.Grupo.domain.Grupo_Repository;

public class DeleteGrupo_UseCase {
    private final Grupo_Repository repository;

    public DeleteGrupo_UseCase(Grupo_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Void> execute(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("El ID del grupo debe ser válido");
            }
            return id;
        })
        .thenCompose(validId -> 
            // Primero verificar que existe
            repository.getById(validId)
                .thenCompose(grupo -> {
                    if (grupo == null) {
                        throw new RuntimeException("Grupo no encontrado con ID: " + validId);
                    }
                    // Luego eliminar
                    return repository.delete(validId);
                })
        );
    }
}