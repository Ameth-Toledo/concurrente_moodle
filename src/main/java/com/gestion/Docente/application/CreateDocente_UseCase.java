package com.gestion.Docente.application;

import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

import com.gestion.Docente.domain.Docente_Repository;
import com.gestion.Docente.domain.entities.Docente;

public class CreateDocente_UseCase {
    private final Docente_Repository repository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public CreateDocente_UseCase(Docente_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Docente> execute(Docente docente) {
        return CompletableFuture.supplyAsync(() -> {
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

            // Validar teléfono (opcional pero si está presente debe tener formato válido)
            if (docente.getTelefono() != null && !docente.getTelefono().trim().isEmpty()) {
                String telefono = docente.getTelefono().replaceAll("[^0-9]", "");
                if (telefono.length() < 10) {
                    throw new IllegalArgumentException("El teléfono debe tener al menos 10 dígitos");
                }
            }

            return docente;
        })
        .thenCompose(validatedDocente -> 
            // Verificar que el email no exista
            repository.existsByEmail(validatedDocente.getEmail())
                .thenCompose(exists -> {
                    if (exists) {
                        throw new IllegalArgumentException("El email ya está registrado");
                    }
                    // Guardar el docente
                    return repository.save(validatedDocente);
                })
        );
    }
}