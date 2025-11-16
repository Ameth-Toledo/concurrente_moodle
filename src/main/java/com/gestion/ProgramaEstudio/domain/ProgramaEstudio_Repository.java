package com.gestion.ProgramaEstudio.domain;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.ProgramaEstudio.domain.entities.ProgramaEstudio;

public interface ProgramaEstudio_Repository {
    
    CompletableFuture<ProgramaEstudio> save(ProgramaEstudio programaEstudio);
    
    CompletableFuture<List<ProgramaEstudio>> getAll();
    
    CompletableFuture<ProgramaEstudio> getById(Integer id);
    
    CompletableFuture<ProgramaEstudio> getByNombre(String nombre);
    
    CompletableFuture<List<ProgramaEstudio>> searchByName(String searchTerm);
    
    CompletableFuture<Void> update(ProgramaEstudio programaEstudio);
    
    CompletableFuture<Void> delete(Integer id);
    
    CompletableFuture<Boolean> existsByNombre(String nombre);
    
    // Métodos para estadísticas
    CompletableFuture<Integer> countAlumnos(Integer programaId);
    
    CompletableFuture<Integer> countAsignaturas(Integer programaId);
}