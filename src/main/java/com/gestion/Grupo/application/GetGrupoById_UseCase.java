package com.gestion.Grupo.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.Grupo.domain.Grupo_Repository;
import com.gestion.Grupo.domain.entities.Grupo;

public class GetGrupoById_UseCase {
    private final Grupo_Repository repository;

    public GetGrupoById_UseCase(Grupo_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Grupo> execute(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("El ID del grupo debe ser válido");
            }
            return id;
        }).thenCompose(validId -> repository.getById(validId))
          .thenApply(grupo -> {
              if (grupo == null) {
                  throw new RuntimeException("Grupo no encontrado con ID: " + id);
              }
              return grupo;
          });
    }
}