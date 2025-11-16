package com.gestion.Docente.application;

import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

import com.gestion.Docente.domain.Docente_Repository;
import com.gestion.Docente.domain.entities.Docente;

public class UpdateDocente_UseCase {
    private final Docente_Repository repository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public UpdateDocente_UseCase(Docente_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Docente> execute(Integer id, Docente docente) {
        return CompletableFuture.supplyAsync(() -> {
            // Validar ID
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("El ID del docente debe ser válido");
            }

            // Validar nombre
            if (docente.getNombre() == null || docente.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre es obligatorio");
            }

            // Validar apellido
            if (docente.getApellido() == null || docente.getApellido().trim().isEmpty()) {
                throw new IllegalArgumentException("El apellido es obligatorio");
            }

            // Validar email
            if (docente.getEmail() == null || !EMAIL_PATTERN.matcher(docente.getEmail()).matches()) {
                throw new IllegalArgumentException("El email no es válido");
            }

            // Validar teléfono (opcional)
            if (docente.getTelefono() != null && !docente.getTelefono().trim().isEmpty()) {
                String telefono = docente.getTelefono().replaceAll("[^0-9]", "");
                if (telefono.length() < 10) {
                    throw new IllegalArgumentException("El teléfono debe tener al menos 10 dígitos");
                }
            }

            docente.setId(id);
            return docente;
        })
        .thenCompose(validatedDocente -> 
            // Primero verificar que el docente existe
            repository.getById(id)
                .thenCompose(existingDocente -> {
                    if (existingDocente == null) {
                        throw new RuntimeException("Docente no encontrado con ID: " + id);
                    }
                    // Si cambió el email, verificar que no exista
                    if (!existingDocente.getEmail().equals(validatedDocente.getEmail())) {
                        return repository.existsByEmail(validatedDocente.getEmail())
                            .thenCompose(exists -> {
                                if (exists) {
                                    throw new IllegalArgumentException("El email ya está registrado");
                                }
                                return repository.update(validatedDocente)
                                    .thenApply(v -> validatedDocente);
                            });
                    }
                    // Si no cambió el email, actualizar directamente
                    return repository.update(validatedDocente)
                        .thenApply(v -> validatedDocente);
                })
        );
    }
}