package com.gestion.ProgramaEstudio.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.ProgramaEstudio.domain.ProgramaEstudio_Repository;
import com.gestion.ProgramaEstudio.domain.entities.ProgramaEstudio;

public class UpdateProgramaEstudio_UseCase {
    private final ProgramaEstudio_Repository repository;

    public UpdateProgramaEstudio_UseCase(ProgramaEstudio_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<ProgramaEstudio> execute(Integer id, ProgramaEstudio programaEstudio) {
        return CompletableFuture.supplyAsync(() -> {
            // Validar ID
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("El ID del programa debe ser válido");
            }

            // Validar nombre
            if (programaEstudio.getNombre() == null || programaEstudio.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del programa es obligatorio");
            }

            // Validar número de cuatrimestres
            if (programaEstudio.getNumCuatrimestres() == null || 
                programaEstudio.getNumCuatrimestres() < 1 || 
                programaEstudio.getNumCuatrimestres() > 15) {
                throw new IllegalArgumentException("El número de cuatrimestres debe estar entre 1 y 15");
            }

            programaEstudio.setId(id);
            return programaEstudio;
        })
        .thenCompose(validatedPrograma -> 
            // Primero verificar que el programa existe
            repository.getById(id)
                .thenCompose(existingPrograma -> {
                    if (existingPrograma == null) {
                        throw new RuntimeException("Programa de estudio no encontrado con ID: " + id);
                    }
                    // Si cambió el nombre, verificar que no exista
                    if (!existingPrograma.getNombre().equals(validatedPrograma.getNombre())) {
                        return repository.existsByNombre(validatedPrograma.getNombre())
                            .thenCompose(exists -> {
                                if (exists) {
                                    throw new IllegalArgumentException("Ya existe un programa con ese nombre");
                                }
                                return repository.update(validatedPrograma)
                                    .thenApply(v -> validatedPrograma);
                            });
                    }
                    // Si no cambió el nombre, actualizar directamente
                    return repository.update(validatedPrograma)
                        .thenApply(v -> validatedPrograma);
                })
        );
    }
}