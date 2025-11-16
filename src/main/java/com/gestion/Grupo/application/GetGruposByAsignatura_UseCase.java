package com.gestion.Grupo.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.Grupo.domain.Grupo_Repository;
import com.gestion.Grupo.domain.entities.Grupo;

public class GetGruposByAsignatura_UseCase {
    private final Grupo_Repository repository;

    public GetGruposByAsignatura_UseCase(Grupo_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<Grupo>> execute(Integer asignaturaId) {
        return CompletableFuture.supplyAsync(() -> {
            if (asignaturaId == null || asignaturaId <= 0) {
                throw new IllegalArgumentException("El ID de la asignatura debe ser válido");
            }
            return asignaturaId;
        }).thenCompose(validAsignaturaId -> repository.getByAsignatura(validAsignaturaId))
          .thenApply(grupos -> {
              if (grupos.isEmpty()) {
                  throw new RuntimeException("No se encontraron grupos para la asignatura con ID: " + asignaturaId);
              }
              return grupos;
          });
    }
}