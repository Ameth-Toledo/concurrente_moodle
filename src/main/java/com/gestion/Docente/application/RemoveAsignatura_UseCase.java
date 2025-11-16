package com.gestion.Docente.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.Docente.domain.Docente_Repository;

public class RemoveAsignatura_UseCase {
    private final Docente_Repository repository;

    public RemoveAsignatura_UseCase(Docente_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Void> execute(Integer docenteId, Integer asignaturaId) {
        return CompletableFuture.supplyAsync(() -> {
            if (docenteId == null || docenteId <= 0) {
                throw new IllegalArgumentException("El ID del docente debe ser válido");
            }
            if (asignaturaId == null || asignaturaId <= 0) {
                throw new IllegalArgumentException("El ID de la asignatura debe ser válido");
            }
            return docenteId;
        })
        .thenCompose(validDocenteId -> 
            // Verificar que el docente existe
            repository.getById(validDocenteId)
                .thenCompose(docente -> {
                    if (docente == null) {
                        throw new RuntimeException("Docente no encontrado con ID: " + validDocenteId);
                    }
                    // Remover la asignatura
                    return repository.removeAsignatura(validDocenteId, asignaturaId);
                })
        );
    }
}