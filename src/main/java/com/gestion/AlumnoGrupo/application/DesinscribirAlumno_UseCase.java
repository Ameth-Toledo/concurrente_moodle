package com.gestion.AlumnoGrupo.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.AlumnoGrupo.domain.AlumnoGrupo_Repository;

public class DesinscribirAlumno_UseCase {
    private final AlumnoGrupo_Repository repository;

    public DesinscribirAlumno_UseCase(AlumnoGrupo_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Void> execute(Integer alumnoId, Integer grupoId) {
        return CompletableFuture.supplyAsync(() -> {
            // Validar IDs
            if (alumnoId == null || alumnoId <= 0) {
                throw new IllegalArgumentException("El ID del alumno debe ser válido");
            }
            if (grupoId == null || grupoId <= 0) {
                throw new IllegalArgumentException("El ID del grupo debe ser válido");
            }
            return alumnoId;
        })
        .thenCompose(validAlumnoId -> 
            // Verificar que la inscripción existe
            repository.existsInscripcion(validAlumnoId, grupoId)
                .thenCompose(exists -> {
                    if (!exists) {
                        throw new RuntimeException("El alumno no está inscrito en este grupo");
                    }
                    // Desinscribir al alumno
                    return repository.desinscribir(validAlumnoId, grupoId);
                })
        );
    }
}