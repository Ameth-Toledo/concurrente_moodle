package com.gestion.Alumno.application;

import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

import com.gestion.Alumno.domain.Alumno_Repository;
import com.gestion.Alumno.domain.entities.Alumno;

public class UpdateAlumno_UseCase {
    private final Alumno_Repository repository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public UpdateAlumno_UseCase(Alumno_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Alumno> execute(Integer id, Alumno alumno) {
        return CompletableFuture.supplyAsync(() -> {
            // Validar ID
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("El ID del alumno debe ser válido");
            }

            // Validar nombre
            if (alumno.getNombre() == null || alumno.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre es obligatorio");
            }

            // Validar apellido
            if (alumno.getApellido() == null || alumno.getApellido().trim().isEmpty()) {
                throw new IllegalArgumentException("El apellido es obligatorio");
            }

            // Validar matrícula
            if (alumno.getMatricula() == null || alumno.getMatricula().trim().isEmpty()) {
                throw new IllegalArgumentException("La matrícula es obligatoria");
            }

            // Validar email
            if (alumno.getEmail() == null || !EMAIL_PATTERN.matcher(alumno.getEmail()).matches()) {
                throw new IllegalArgumentException("El email no es válido");
            }

            // Validar cuatrimestre
            if (alumno.getCuatrimestre() == null || alumno.getCuatrimestre() < 1 || alumno.getCuatrimestre() > 10) {
                throw new IllegalArgumentException("El cuatrimestre debe estar entre 1 y 10");
            }

            // Validar programa
            if (alumno.getProgramaId() == null || alumno.getProgramaId() <= 0) {
                throw new IllegalArgumentException("El ID del programa es obligatorio");
            }

            alumno.setId(id);
            return alumno;
        })
        .thenCompose(validatedAlumno -> 
            // Primero verificar que el alumno existe
            repository.getById(id)
                .thenCompose(existingAlumno -> {
                    if (existingAlumno == null) {
                        throw new RuntimeException("Alumno no encontrado con ID: " + id);
                    }
                    // Si cambió la matrícula, verificar que no exista
                    if (!existingAlumno.getMatricula().equals(validatedAlumno.getMatricula())) {
                        return repository.existsByMatricula(validatedAlumno.getMatricula())
                            .thenCompose(exists -> {
                                if (exists) {
                                    throw new IllegalArgumentException("La matrícula ya está registrada");
                                }
                                return CompletableFuture.completedFuture(existingAlumno);
                            });
                    }
                    return CompletableFuture.completedFuture(existingAlumno);
                })
                .thenCompose(existingAlumno -> {
                    // Si cambió el email, verificar que no exista
                    if (!existingAlumno.getEmail().equals(validatedAlumno.getEmail())) {
                        return repository.existsByEmail(validatedAlumno.getEmail())
                            .thenCompose(exists -> {
                                if (exists) {
                                    throw new IllegalArgumentException("El email ya está registrado");
                                }
                                return repository.update(validatedAlumno)
                                    .thenApply(v -> validatedAlumno);
                            });
                    }
                    // Si no cambió el email, actualizar directamente
                    return repository.update(validatedAlumno)
                        .thenApply(v -> validatedAlumno);
                })
        );
    }
}