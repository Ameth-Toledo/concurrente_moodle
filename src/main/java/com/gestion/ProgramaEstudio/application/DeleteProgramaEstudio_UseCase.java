package com.gestion.ProgramaEstudio.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.ProgramaEstudio.domain.ProgramaEstudio_Repository;

public class DeleteProgramaEstudio_UseCase {
    private final ProgramaEstudio_Repository repository;

    public DeleteProgramaEstudio_UseCase(ProgramaEstudio_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Void> execute(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("El ID del programa debe ser válido");
            }
            return id;
        })
        .thenCompose(validId -> 
            // Primero verificar que existe
            repository.getById(validId)
                .thenCompose(programa -> {
                    if (programa == null) {
                        throw new RuntimeException("Programa de estudio no encontrado con ID: " + validId);
                    }
                    // Verificar que no tenga alumnos
                    return repository.countAlumnos(validId)
                        .thenCompose(countAlumnos -> {
                            if (countAlumnos > 0) {
                                throw new IllegalArgumentException(
                                    "No se puede eliminar el programa porque tiene " + countAlumnos + " alumnos inscritos");
                            }
                            // Luego eliminar
                            return repository.delete(validId);
                        });
                })
        );
    }
}