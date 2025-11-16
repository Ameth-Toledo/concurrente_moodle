package com.gestion.ProgramaEstudio.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.ProgramaEstudio.domain.ProgramaEstudio_Repository;
import com.gestion.ProgramaEstudio.domain.entities.ProgramaEstudio;

public class CreateProgramaEstudio_UseCase {
    private final ProgramaEstudio_Repository repository;

    public CreateProgramaEstudio_UseCase(ProgramaEstudio_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<ProgramaEstudio> execute(ProgramaEstudio programaEstudio) {
        return CompletableFuture.supplyAsync(() -> {
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

            return programaEstudio;
        })
        .thenCompose(validatedPrograma -> 
            // Verificar que no exista un programa con el mismo nombre
            repository.existsByNombre(validatedPrograma.getNombre())
                .thenCompose(exists -> {
                    if (exists) {
                        throw new IllegalArgumentException("Ya existe un programa con ese nombre");
                    }
                    // Guardar el programa
                    return repository.save(validatedPrograma);
                })
        );
    }
}