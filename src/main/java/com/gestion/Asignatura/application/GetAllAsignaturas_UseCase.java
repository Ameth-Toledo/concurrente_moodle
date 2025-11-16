package com.gestion.Asignatura.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.Asignatura.domain.Asignatura_Repository;
import com.gestion.Asignatura.domain.entities.Asignatura;

public class GetAllAsignaturas_UseCase {
    private final Asignatura_Repository repository;

    public GetAllAsignaturas_UseCase(Asignatura_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<Asignatura>> execute() {
        return repository.getAll()
            .thenApply(asignaturas -> {
                if (asignaturas.isEmpty()) {
                    throw new RuntimeException("No se encontraron asignaturas");
                }
                return asignaturas;
            });
    }
}