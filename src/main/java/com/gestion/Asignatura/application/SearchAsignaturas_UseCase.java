package com.gestion.Asignatura.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.Asignatura.domain.Asignatura_Repository;
import com.gestion.Asignatura.domain.entities.Asignatura;

public class SearchAsignaturas_UseCase {
    private final Asignatura_Repository repository;

    public SearchAsignaturas_UseCase(Asignatura_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<Asignatura>> execute(String searchTerm) {
        return CompletableFuture.supplyAsync(() -> {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                throw new IllegalArgumentException("El término de búsqueda es obligatorio");
            }
            if (searchTerm.trim().length() < 2) {
                throw new IllegalArgumentException("El término de búsqueda debe tener al menos 2 caracteres");
            }
            return searchTerm.trim();
        }).thenCompose(validSearchTerm -> repository.searchByName(validSearchTerm))
          .thenApply(asignaturas -> {
              if (asignaturas.isEmpty()) {
                  throw new RuntimeException("No se encontraron asignaturas con el término: " + searchTerm);
              }
              return asignaturas;
          });
    }
}