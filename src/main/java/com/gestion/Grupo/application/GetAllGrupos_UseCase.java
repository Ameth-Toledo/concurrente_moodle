package com.gestion.Grupo.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.Grupo.domain.Grupo_Repository;
import com.gestion.Grupo.domain.entities.Grupo;

public class GetAllGrupos_UseCase {
    private final Grupo_Repository repository;

    public GetAllGrupos_UseCase(Grupo_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<Grupo>> execute() {
        return repository.getAll()
            .thenApply(grupos -> {
                if (grupos.isEmpty()) {
                    throw new RuntimeException("No se encontraron grupos");
                }
                return grupos;
            });
    }
}