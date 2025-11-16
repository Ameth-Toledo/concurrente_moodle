package com.gestion.Alumno.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.Alumno.domain.Alumno_Repository;
import com.gestion.Alumno.domain.entities.Alumno;

public class GetAllAlumnos_UseCase {
    private final Alumno_Repository repository;

    public GetAllAlumnos_UseCase(Alumno_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<Alumno>> execute() {
        return repository.getAll()
            .thenApply(alumnos -> {
                if (alumnos.isEmpty()) {
                    throw new RuntimeException("No se encontraron alumnos");
                }
                return alumnos;
            });
    }
}