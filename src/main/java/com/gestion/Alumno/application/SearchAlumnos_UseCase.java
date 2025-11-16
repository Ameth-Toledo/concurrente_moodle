package com.gestion.Alumno.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.Alumno.domain.Alumno_Repository;
import com.gestion.Alumno.domain.entities.Alumno;

public class SearchAlumnos_UseCase {
    private final Alumno_Repository repository;

    public SearchAlumnos_UseCase(Alumno_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<Alumno>> execute(String searchTerm) {
        return CompletableFuture.supplyAsync(() -> {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                throw new IllegalArgumentException("El término de búsqueda es obligatorio");
            }
            if (searchTerm.trim().length() < 2) {
                throw new IllegalArgumentException("El término de búsqueda debe tener al menos 2 caracteres");
            }
            return searchTerm.trim();
        }).thenCompose(validSearchTerm -> repository.searchByName(validSearchTerm))
          .thenApply(alumnos -> {
              if (alumnos.isEmpty()) {
                  throw new RuntimeException("No se encontraron alumnos con el término: " + searchTerm);
              }
              return alumnos;
          });
    }
}