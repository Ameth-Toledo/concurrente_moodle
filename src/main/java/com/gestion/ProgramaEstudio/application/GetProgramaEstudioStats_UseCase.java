package com.gestion.ProgramaEstudio.application;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.gestion.ProgramaEstudio.domain.ProgramaEstudio_Repository;

public class GetProgramaEstudioStats_UseCase {
    private final ProgramaEstudio_Repository repository;

    public GetProgramaEstudioStats_UseCase(ProgramaEstudio_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Map<String, Object>> execute(Integer programaId) {
        return CompletableFuture.supplyAsync(() -> {
            if (programaId == null || programaId <= 0) {
                throw new IllegalArgumentException("El ID del programa debe ser válido");
            }
            return programaId;
        })
        .thenCompose(validId -> 
            // Verificar que el programa existe
            repository.getById(validId)
                .thenCompose(programa -> {
                    if (programa == null) {
                        throw new RuntimeException("Programa de estudio no encontrado con ID: " + validId);
                    }
                    
                    // Obtener estadísticas en paralelo
                    CompletableFuture<Integer> alumnos = repository.countAlumnos(validId);
                    CompletableFuture<Integer> asignaturas = repository.countAsignaturas(validId);
                    
                    return CompletableFuture.allOf(alumnos, asignaturas)
                        .thenApply(v -> {
                            Map<String, Object> stats = new HashMap<>();
                            stats.put("programaId", validId);
                            stats.put("nombre", programa.getNombre());
                            stats.put("numCuatrimestres", programa.getNumCuatrimestres());
                            stats.put("totalAlumnos", alumnos.join());
                            stats.put("totalAsignaturas", asignaturas.join());
                            stats.put("fechaCreacion", programa.getFechaCreacion());
                            return stats;
                        });
                })
        );
    }
}