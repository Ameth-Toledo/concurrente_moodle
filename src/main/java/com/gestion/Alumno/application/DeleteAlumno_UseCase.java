package com.gestion.Alumno.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.Alumno.domain.Alumno_Repository;

public class DeleteAlumno_UseCase {
    private final Alumno_Repository repository;

    public DeleteAlumno_UseCase(Alumno_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Void> execute(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("El ID del alumno debe ser válido");
            }
            return id;
        })
        .thenCompose(validId -> 
            // Primero verificar que existe
            repository.getById(validId)
                .thenCompose(alumno -> {
                    if (alumno == null) {
                        throw new RuntimeException("Alumno no encontrado con ID: " + validId);
                    }
                    // Luego eliminar
                    return repository.delete(validId);
                })
        );
    }
}