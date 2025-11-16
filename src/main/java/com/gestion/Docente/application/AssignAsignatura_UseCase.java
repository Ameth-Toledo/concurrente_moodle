package com.gestion.Docente.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.Docente.domain.Docente_Repository;

public class AssignAsignatura_UseCase {
    private final Docente_Repository repository;

    public AssignAsignatura_UseCase(Docente_Repository repository) {
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
                    // Asignar la asignatura
                    return repository.assignAsignatura(validDocenteId, asignaturaId);
                })
        );
    }
}