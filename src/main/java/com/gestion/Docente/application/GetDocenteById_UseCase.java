package com.gestion.Docente.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.Docente.domain.Docente_Repository;
import com.gestion.Docente.domain.entities.Docente;

public class GetDocenteById_UseCase {
    private final Docente_Repository repository;

    public GetDocenteById_UseCase(Docente_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Docente> execute(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("El ID del docente debe ser válido");
            }
            return id;
        }).thenCompose(validId -> repository.getById(validId))
          .thenApply(docente -> {
              if (docente == null) {
                  throw new RuntimeException("Docente no encontrado con ID: " + id);
              }
              return docente;
          });
    }
}