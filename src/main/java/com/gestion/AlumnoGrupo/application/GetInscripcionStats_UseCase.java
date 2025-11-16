package com.gestion.AlumnoGrupo.application;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.gestion.AlumnoGrupo.domain.AlumnoGrupo_Repository;

public class GetInscripcionStats_UseCase {
    private final AlumnoGrupo_Repository repository;

    public GetInscripcionStats_UseCase(AlumnoGrupo_Repository repository) {
        this.repository = repository;
    }

    // Obtener estadísticas de un grupo
    public CompletableFuture<Map<String, Object>> getGrupoStats(Integer grupoId) {
        return CompletableFuture.supplyAsync(() -> {
            if (grupoId == null || grupoId <= 0) {
                throw new IllegalArgumentException("El ID del grupo debe ser válido");
            }
            return grupoId;
        })
        .thenCompose(validGrupoId -> 
            repository.countAlumnosInGrupo(validGrupoId)
                .thenCombine(repository.hasCapacity(validGrupoId), (count, hasCapacity) -> {
                    Map<String, Object> stats = new HashMap<>();
                    stats.put("grupoId", validGrupoId);
                    stats.put("totalAlumnos", count);
                    stats.put("tieneCapacidad", hasCapacity);
                    return stats;
                })
        );
    }

    // Obtener estadísticas de un alumno
    public CompletableFuture<Map<String, Object>> getAlumnoStats(Integer alumnoId) {
        return CompletableFuture.supplyAsync(() -> {
            if (alumnoId == null || alumnoId <= 0) {
                throw new IllegalArgumentException("El ID del alumno debe ser válido");
            }
            return alumnoId;
        })
        .thenCompose(validAlumnoId -> 
            repository.countGruposOfAlumno(validAlumnoId)
                .thenApply(count -> {
                    Map<String, Object> stats = new HashMap<>();
                    stats.put("alumnoId", validAlumnoId);
                    stats.put("totalGrupos", count);
                    return stats;
                })
        );
    }
}