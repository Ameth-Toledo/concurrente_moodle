package com.gestion.Asignatura.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.Asignatura.domain.Asignatura_Repository;
import com.gestion.Asignatura.domain.entities.Asignatura;

public class GetAsignaturasByCuatrimestre_UseCase {
    private final Asignatura_Repository repository;

    public GetAsignaturasByCuatrimestre_UseCase(Asignatura_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<Asignatura>> execute(Integer cuatrimestre) {
        return CompletableFuture.supplyAsync(() -> {
            if (cuatrimestre == null || cuatrimestre < 1 || cuatrimestre > 10) {
                throw new IllegalArgumentException("El cuatrimestre debe estar entre 1 y 10");
            }
            return cuatrimestre;
        }).thenCompose(validCuatrimestre -> repository.getByCuatrimestre(validCuatrimestre))
          .thenApply(asignaturas -> {
              if (asignaturas.isEmpty()) {
                  throw new RuntimeException("No se encontraron asignaturas para el cuatrimestre: " + cuatrimestre);
              }
              return asignaturas;
          });
    }
}