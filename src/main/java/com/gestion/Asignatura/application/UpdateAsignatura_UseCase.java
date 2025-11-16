package com.gestion.Asignatura.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.Asignatura.domain.Asignatura_Repository;
import com.gestion.Asignatura.domain.entities.Asignatura;

public class UpdateAsignatura_UseCase {
    private final Asignatura_Repository repository;

    public UpdateAsignatura_UseCase(Asignatura_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Asignatura> execute(Integer id, Asignatura asignatura) {
        return CompletableFuture.supplyAsync(() -> {
            // Validar ID
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("El ID de la asignatura debe ser válido");
            }

            // Validar nombre
            if (asignatura.getNombre() == null || asignatura.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre de la asignatura es obligatorio");
            }

            // Validar cuatrimestre
            if (asignatura.getCuatrimestre() == null || asignatura.getCuatrimestre() < 1 || asignatura.getCuatrimestre() > 10) {
                throw new IllegalArgumentException("El cuatrimestre debe estar entre 1 y 10");
            }

            // Validar programa
            if (asignatura.getProgramaId() == null || asignatura.getProgramaId() <= 0) {
                throw new IllegalArgumentException("El ID del programa es obligatorio");
            }

            // Validar créditos
            if (asignatura.getCreditos() == null || asignatura.getCreditos() < 1 || asignatura.getCreditos() > 12) {
                throw new IllegalArgumentException("Los créditos deben estar entre 1 y 12");
            }

            asignatura.setId(id);
            return asignatura;
        })
        .thenCompose(validatedAsignatura -> 
            // Primero verificar que la asignatura existe
            repository.getById(id)
                .thenCompose(existingAsignatura -> {
                    if (existingAsignatura == null) {
                        throw new RuntimeException("Asignatura no encontrada con ID: " + id);
                    }
                    // Si cambió el nombre o programa, verificar que no exista
                    if (!existingAsignatura.getNombre().equals(validatedAsignatura.getNombre()) ||
                        !existingAsignatura.getProgramaId().equals(validatedAsignatura.getProgramaId())) {
                        return repository.existsByNombre(validatedAsignatura.getNombre(), validatedAsignatura.getProgramaId())
                            .thenCompose(exists -> {
                                if (exists) {
                                    throw new IllegalArgumentException("Ya existe una asignatura con ese nombre en este programa");
                                }
                                return repository.update(validatedAsignatura)
                                    .thenApply(v -> validatedAsignatura);
                            });
                    }
                    // Si no cambió el nombre ni programa, actualizar directamente
                    return repository.update(validatedAsignatura)
                        .thenApply(v -> validatedAsignatura);
                })
        );
    }
}