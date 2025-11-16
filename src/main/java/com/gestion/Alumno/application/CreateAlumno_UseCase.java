package com.gestion.Alumno.application;

import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

import com.gestion.Alumno.domain.Alumno_Repository;
import com.gestion.Alumno.domain.entities.Alumno;

public class CreateAlumno_UseCase {
    private final Alumno_Repository repository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public CreateAlumno_UseCase(Alumno_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Alumno> execute(Alumno alumno) {
        return CompletableFuture.supplyAsync(() -> {
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

            return alumno;
        })
        .thenCompose(validatedAlumno -> 
            // Verificar que la matrícula no exista
            repository.existsByMatricula(validatedAlumno.getMatricula())
                .thenCompose(exists -> {
                    if (exists) {
                        throw new IllegalArgumentException("La matrícula ya está registrada");
                    }
                    // Verificar que el email no exista
                    return repository.existsByEmail(validatedAlumno.getEmail());
                })
                .thenCompose(exists -> {
                    if (exists) {
                        throw new IllegalArgumentException("El email ya está registrado");
                    }
                    // Guardar el alumno
                    return repository.save(validatedAlumno);
                })
        );
    }
}