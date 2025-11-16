package com.gestion.Docente.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.Docente.domain.Docente_Repository;
import com.gestion.Docente.domain.entities.Docente;

public class GetAllDocentes_UseCase {
    private final Docente_Repository repository;

    public GetAllDocentes_UseCase(Docente_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<Docente>> execute() {
        return repository.getAll()
            .thenApply(docentes -> {
                if (docentes.isEmpty()) {
                    throw new RuntimeException("No se encontraron docentes");
                }
                return docentes;
            });
    }
}