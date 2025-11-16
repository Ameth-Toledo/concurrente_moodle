package com.gestion.ProgramaEstudio.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.ProgramaEstudio.domain.ProgramaEstudio_Repository;
import com.gestion.ProgramaEstudio.domain.entities.ProgramaEstudio;

public class GetAllProgramasEstudio_UseCase {
    private final ProgramaEstudio_Repository repository;

    public GetAllProgramasEstudio_UseCase(ProgramaEstudio_Repository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<ProgramaEstudio>> execute() {
        return repository.getAll()
            .thenApply(programas -> {
                if (programas.isEmpty()) {
                    throw new RuntimeException("No se encontraron programas de estudio");
                }
                return programas;
            });
    }
}