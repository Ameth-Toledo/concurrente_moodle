package com.gestion.ProgramaEstudio.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.ProgramaEstudio.domain.ProgramaEstudio_Repository;
import com.gestion.ProgramaEstudio.domain.entities.ProgramaEstudio;

public class GetProgramaEstudioById_UseCase {
    private final ProgramaEstudio_Repository repository;

    public GetProgramaEstudioById_UseCase(ProgramaEstudio_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<ProgramaEstudio> execute(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("El ID del programa debe ser válido");
            }
            return id;
        }).thenCompose(validId -> repository.getById(validId))
          .thenApply(programa -> {
              if (programa == null) {
                  throw new RuntimeException("Programa de estudio no encontrado con ID: " + id);
              }
              return programa;
          });
    }
}