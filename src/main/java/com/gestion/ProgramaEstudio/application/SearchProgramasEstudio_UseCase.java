package com.gestion.ProgramaEstudio.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.ProgramaEstudio.domain.ProgramaEstudio_Repository;
import com.gestion.ProgramaEstudio.domain.entities.ProgramaEstudio;

public class SearchProgramasEstudio_UseCase {
    private final ProgramaEstudio_Repository repository;

    public SearchProgramasEstudio_UseCase(ProgramaEstudio_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<ProgramaEstudio>> execute(String searchTerm) {
        return CompletableFuture.supplyAsync(() -> {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                throw new IllegalArgumentException("El término de búsqueda es obligatorio");
            }
            if (searchTerm.trim().length() < 2) {
                throw new IllegalArgumentException("El término de búsqueda debe tener al menos 2 caracteres");
            }
            return searchTerm.trim();
        }).thenCompose(validSearchTerm -> repository.searchByName(validSearchTerm))
          .thenApply(programas -> {
              if (programas.isEmpty()) {
                  throw new RuntimeException("No se encontraron programas con el término: " + searchTerm);
              }
              return programas;
          });
    }
}