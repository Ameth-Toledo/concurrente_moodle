package com.gestion.Asignatura.application;

import java.util.concurrent.CompletableFuture;

import com.gestion.Asignatura.domain.Asignatura_Repository;
import com.gestion.Asignatura.domain.entities.Asignatura;

public class CreateAsignatura_UseCase {
    private final Asignatura_Repository repository;

    public CreateAsignatura_UseCase(Asignatura_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Asignatura> execute(Asignatura asignatura) {
        return CompletableFuture.supplyAsync(() -> {
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

            return asignatura;
        })
        .thenCompose(validatedAsignatura -> 
            // Verificar que no exista una asignatura con el mismo nombre en el mismo programa
            repository.existsByNombre(validatedAsignatura.getNombre(), validatedAsignatura.getProgramaId())
                .thenCompose(exists -> {
                    if (exists) {
                        throw new IllegalArgumentException("Ya existe una asignatura con ese nombre en este programa");
                    }
                    // Guardar la asignatura
                    return repository.save(validatedAsignatura);
                })
        );
    }
}