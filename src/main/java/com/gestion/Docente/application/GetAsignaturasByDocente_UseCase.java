package com.gestion.Docente.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.Docente.domain.Docente_Repository;

public class GetAsignaturasByDocente_UseCase {
    private final Docente_Repository repository;

    public GetAsignaturasByDocente_UseCase(Docente_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<Integer>> execute(Integer docenteId) {
        return CompletableFuture.supplyAsync(() -> {
            if (docenteId == null || docenteId <= 0) {
                throw new IllegalArgumentException("El ID del docente debe ser válido");
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
                    // Obtener las asignaturas
                    return repository.getAsignaturasByDocente(validDocenteId);
                })
        );
    }
}