package com.gestion.Asignatura.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.Asignatura.domain.Asignatura_Repository;
import com.gestion.Asignatura.domain.entities.Asignatura;

public class GetAsignaturasByPrograma_UseCase {
    private final Asignatura_Repository repository;

    public GetAsignaturasByPrograma_UseCase(Asignatura_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<Asignatura>> execute(Integer programaId) {
        return CompletableFuture.supplyAsync(() -> {
            if (programaId == null || programaId <= 0) {
                throw new IllegalArgumentException("El ID del programa debe ser válido");
            }
            return programaId;
        }).thenCompose(validProgramaId -> repository.getByPrograma(validProgramaId))
          .thenApply(asignaturas -> {
              if (asignaturas.isEmpty()) {
                  throw new RuntimeException("No se encontraron asignaturas para el programa con ID: " + programaId);
              }
              return asignaturas;
          });
    }
}