package com.gestion.AlumnoGrupo.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.AlumnoGrupo.domain.AlumnoGrupo_Repository;

public class GetAlumnosByGrupo_UseCase {
    private final AlumnoGrupo_Repository repository;

    public GetAlumnosByGrupo_UseCase(AlumnoGrupo_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<Integer>> execute(Integer grupoId) {
        return CompletableFuture.supplyAsync(() -> {
            if (grupoId == null || grupoId <= 0) {
                throw new IllegalArgumentException("El ID del grupo debe ser válido");
            }
            return grupoId;
        }).thenCompose(validGrupoId -> repository.getAlumnosByGrupo(validGrupoId));
    }
}