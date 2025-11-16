package com.gestion.Asignatura.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.Asignatura.domain.Asignatura_Repository;
import com.gestion.Asignatura.domain.entities.Asignatura;

public class GetAsignaturaById_UseCase {
    private final Asignatura_Repository repository;

    public GetAsignaturaById_UseCase(Asignatura_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Asignatura> execute(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("El ID de la asignatura debe ser válido");
            }
            return id;
        }).thenCompose(validId -> repository.getById(validId))
          .thenApply(asignatura -> {
              if (asignatura == null) {
                  throw new RuntimeException("Asignatura no encontrada con ID: " + id);
              }
              return asignatura;
          });
    }
}