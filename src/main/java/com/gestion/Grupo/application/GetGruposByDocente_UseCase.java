package com.gestion.Grupo.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.Grupo.domain.Grupo_Repository;
import com.gestion.Grupo.domain.entities.Grupo;

public class GetGruposByDocente_UseCase {
    private final Grupo_Repository repository;

    public GetGruposByDocente_UseCase(Grupo_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<Grupo>> execute(Integer docenteId) {
        return CompletableFuture.supplyAsync(() -> {
            if (docenteId == null || docenteId <= 0) {
                throw new IllegalArgumentException("El ID del docente debe ser válido");
            }
            return docenteId;
        }).thenCompose(validDocenteId -> repository.getByDocente(validDocenteId))
          .thenApply(grupos -> {
              if (grupos.isEmpty()) {
                  throw new RuntimeException("No se encontraron grupos para el docente con ID: " + docenteId);
              }
              return grupos;
          });
    }
}