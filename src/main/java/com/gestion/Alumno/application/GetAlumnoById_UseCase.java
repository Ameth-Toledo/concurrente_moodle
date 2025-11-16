package com.gestion.Alumno.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.Alumno.domain.Alumno_Repository;
import com.gestion.Alumno.domain.entities.Alumno;

public class GetAlumnoById_UseCase {
    private final Alumno_Repository repository;

    public GetAlumnoById_UseCase(Alumno_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Alumno> execute(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("El ID del alumno debe ser válido");
            }
            return id;
        }).thenCompose(validId -> repository.getById(validId))
          .thenApply(alumno -> {
              if (alumno == null) {
                  throw new RuntimeException("Alumno no encontrado con ID: " + id);
              }
              return alumno;
          });
    }
}