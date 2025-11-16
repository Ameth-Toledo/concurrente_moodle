package com.gestion.AlumnoGrupo.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.AlumnoGrupo.domain.AlumnoGrupo_Repository;
import com.gestion.AlumnoGrupo.domain.entities.AlumnoGrupo;

public class InscribirAlumno_UseCase {
    private final AlumnoGrupo_Repository repository;

    public InscribirAlumno_UseCase(AlumnoGrupo_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<AlumnoGrupo> execute(Integer alumnoId, Integer grupoId) {
        return CompletableFuture.supplyAsync(() -> {
            // Validar IDs
            if (alumnoId == null || alumnoId <= 0) {
                throw new IllegalArgumentException("El ID del alumno debe ser válido");
            }
            if (grupoId == null || grupoId <= 0) {
                throw new IllegalArgumentException("El ID del grupo debe ser válido");
            }
            
            return new AlumnoGrupo(alumnoId, grupoId);
        })
        .thenCompose(alumnoGrupo -> 
            // Verificar que no esté ya inscrito
            repository.existsInscripcion(alumnoId, grupoId)
                .thenCompose(exists -> {
                    if (exists) {
                        throw new IllegalArgumentException("El alumno ya está inscrito en este grupo");
                    }
                    // Verificar que el grupo tenga capacidad
                    return repository.hasCapacity(grupoId);
                })
                .thenCompose(hasCapacity -> {
                    if (!hasCapacity) {
                        throw new IllegalArgumentException("El grupo ha alcanzado su capacidad máxima");
                    }
                    // Inscribir al alumno
                    return repository.inscribir(alumnoGrupo);
                })
        );
    }
}