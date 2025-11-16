package com.gestion.Docente.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.Docente.domain.Docente_Repository;
import com.gestion.Docente.domain.entities.Docente;

public class SearchDocentes_UseCase {
    private final Docente_Repository repository;

    public SearchDocentes_UseCase(Docente_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<Docente>> execute(String searchTerm) {
        return CompletableFuture.supplyAsync(() -> {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                throw new IllegalArgumentException("El término de búsqueda es obligatorio");
            }
            if (searchTerm.trim().length() < 2) {
                throw new IllegalArgumentException("El término de búsqueda debe tener al menos 2 caracteres");
            }
            return searchTerm.trim();
        }).thenCompose(validSearchTerm -> repository.searchByName(validSearchTerm))
          .thenApply(docentes -> {
              if (docentes.isEmpty()) {
                  throw new RuntimeException("No se encontraron docentes con el término: " + searchTerm);
              }
              return docentes;
          });
    }
}