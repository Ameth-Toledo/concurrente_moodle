package com.gestion.Grupo.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.Grupo.domain.Grupo_Repository;
import com.gestion.Grupo.domain.entities.Grupo;

public class UpdateGrupo_UseCase {
    private final Grupo_Repository repository;

    public UpdateGrupo_UseCase(Grupo_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Grupo> execute(Integer id, Grupo grupo) {
        return CompletableFuture.supplyAsync(() -> {
            // Validar ID
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("El ID del grupo debe ser válido");
            }

            // Validar datos del grupo
            if (grupo.getNombre() == null || grupo.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del grupo es obligatorio");
            }

            if (grupo.getAsignaturaId() == null || grupo.getAsignaturaId() <= 0) {
                throw new IllegalArgumentException("El ID de la asignatura es obligatorio y debe ser válido");
            }

            if (grupo.getDocenteId() == null || grupo.getDocenteId() <= 0) {
                throw new IllegalArgumentException("El ID del docente es obligatorio y debe ser válido");
            }

            if (grupo.getCuatrimestre() == null || grupo.getCuatrimestre() < 1 || grupo.getCuatrimestre() > 10) {
                throw new IllegalArgumentException("El cuatrimestre debe estar entre 1 y 10");
            }

            if (grupo.getCapacidadMaxima() == null || grupo.getCapacidadMaxima() <= 0) {
                grupo.setCapacidadMaxima(25);
            }

            grupo.setId(id);
            return grupo;
        })
        .thenCompose(validatedGrupo -> 
            // Primero verificar que existe
            repository.getById(id)
                .thenCompose(existingGrupo -> {
                    if (existingGrupo == null) {
                        throw new RuntimeException("Grupo no encontrado con ID: " + id);
                    }
                    // Luego actualizar
                    return repository.update(validatedGrupo)
                        .thenApply(v -> validatedGrupo);
                })
        );
    }
}