package com.gestion.Grupo.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.Grupo.domain.Grupo_Repository;
import com.gestion.Grupo.domain.entities.Grupo;

public class GetGruposByCuatrimestre_UseCase {
    private final Grupo_Repository repository;

    public GetGruposByCuatrimestre_UseCase(Grupo_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<Grupo>> execute(Integer cuatrimestre) {
        return CompletableFuture.supplyAsync(() -> {
            if (cuatrimestre == null || cuatrimestre < 1 || cuatrimestre > 10) {
                throw new IllegalArgumentException("El cuatrimestre debe estar entre 1 y 10");
            }
            return cuatrimestre;
        }).thenCompose(validCuatrimestre -> repository.getByCuatrimestre(validCuatrimestre))
          .thenApply(grupos -> {
              if (grupos.isEmpty()) {
                  throw new RuntimeException("No se encontraron grupos para el cuatrimestre: " + cuatrimestre);
              }
              return grupos;
          });
    }
}