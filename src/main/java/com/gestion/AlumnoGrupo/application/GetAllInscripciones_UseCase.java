package com.gestion.AlumnoGrupo.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.AlumnoGrupo.domain.AlumnoGrupo_Repository;
import com.gestion.AlumnoGrupo.domain.entities.AlumnoGrupo;

public class GetAllInscripciones_UseCase {
    private final AlumnoGrupo_Repository repository;

    public GetAllInscripciones_UseCase(AlumnoGrupo_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<AlumnoGrupo>> execute() {
        return repository.getAll()
            .thenApply(inscripciones -> {
                if (inscripciones.isEmpty()) {
                    throw new RuntimeException("No se encontraron inscripciones");
                }
                return inscripciones;
            });
    }
}