package com.gestion.Alumno.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.Alumno.domain.Alumno_Repository;
import com.gestion.Alumno.domain.entities.Alumno;

public class GetAlumnoByMatricula_UseCase {
    private final Alumno_Repository repository;

    public GetAlumnoByMatricula_UseCase(Alumno_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Alumno> execute(String matricula) {
        return CompletableFuture.supplyAsync(() -> {
            if (matricula == null || matricula.trim().isEmpty()) {
                throw new IllegalArgumentException("La matrícula es obligatoria");
            }
            return matricula.trim();
        }).thenCompose(validMatricula -> repository.getByMatricula(validMatricula))
          .thenApply(alumno -> {
              if (alumno == null) {
                  throw new RuntimeException("Alumno no encontrado con matrícula: " + matricula);
              }
              return alumno;
          });
    }
}