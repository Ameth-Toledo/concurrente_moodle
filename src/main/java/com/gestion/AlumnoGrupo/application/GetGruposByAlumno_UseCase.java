package com.gestion.AlumnoGrupo.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.AlumnoGrupo.domain.AlumnoGrupo_Repository;

public class GetGruposByAlumno_UseCase {
    private final AlumnoGrupo_Repository repository;

    public GetGruposByAlumno_UseCase(AlumnoGrupo_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<Integer>> execute(Integer alumnoId) {
        return CompletableFuture.supplyAsync(() -> {
            if (alumnoId == null || alumnoId <= 0) {
                throw new IllegalArgumentException("El ID del alumno debe ser válido");
            }
            return alumnoId;
        }).thenCompose(validAlumnoId -> repository.getGruposByAlumno(validAlumnoId));
    }
}