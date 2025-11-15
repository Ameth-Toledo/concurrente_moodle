package src.main.java.com.gestion.Grupo.application;

import src.main.java.com.gestion.Grupo.domain.Grupo_Repository;
import src.main.java.com.gestion.Grupo.domain.entities.Grupo;

import java.util.concurrent.CompletableFuture;

public class CreateGrupo_UseCase {
    private final Grupo_Repository repository;

    public CreateGrupo_UseCase(Grupo_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Grupo> execute(Grupo grupo) {
        return CompletableFuture.supplyAsync(() -> {
            // Validaciones
            if (grupo.getNombre() == null || grupo.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del grupo es obligatorio");
            }

            if (grupo.getAsignaturaId() == null) {
                throw new IllegalArgumentException("El ID de la asignatura es obligatorio");
            }

            if (grupo.getDocenteId() == null) {
                throw new IllegalArgumentException("El ID del docente es obligatorio");
            }

            if (grupo.getCuatrimestre() == null) {
                throw new IllegalArgumentException("El cuatrimestre es obligatorio");
            }

            if (grupo.getCuatrimestre() < 1 || grupo.getCuatrimestre() > 10) {
                throw new IllegalArgumentException("El cuatrimestre debe estar entre 1 y 10");
            }

            if (grupo.getCapacidadMaxima() == null || grupo.getCapacidadMaxima() <= 0) {
                grupo.setCapacidadMaxima(25);
            }

            return grupo;
        }).thenCompose(validatedGrupo -> repository.save(validatedGrupo));
    }
}