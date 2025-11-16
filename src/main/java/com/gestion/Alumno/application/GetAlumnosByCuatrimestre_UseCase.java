package com.gestion.Alumno.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.Alumno.domain.Alumno_Repository;
import com.gestion.Alumno.domain.entities.Alumno;

public class GetAlumnosByCuatrimestre_UseCase {
    private final Alumno_Repository repository;

    public GetAlumnosByCuatrimestre_UseCase(Alumno_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<Alumno>> execute(Integer cuatrimestre) {
        return CompletableFuture.supplyAsync(() -> {
            if (cuatrimestre == null || cuatrimestre < 1 || cuatrimestre > 10) {
                throw new IllegalArgumentException("El cuatrimestre debe estar entre 1 y 10");
            }
            return cuatrimestre;
        }).thenCompose(validCuatrimestre -> repository.getByCuatrimestre(validCuatrimestre))
          .thenApply(alumnos -> {
              if (alumnos.isEmpty()) {
                  throw new RuntimeException("No se encontraron alumnos para el cuatrimestre: " + cuatrimestre);
              }
              return alumnos;
          });
    }
}