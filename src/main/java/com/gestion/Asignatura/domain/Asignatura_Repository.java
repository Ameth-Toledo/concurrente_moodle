package com.gestion.Asignatura.domain;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.Asignatura.domain.entities.Asignatura;

public interface Asignatura_Repository {
    
    CompletableFuture<Asignatura> save(Asignatura asignatura);
    
    CompletableFuture<List<Asignatura>> getAll();
    
    CompletableFuture<Asignatura> getById(Integer id);
    
    CompletableFuture<List<Asignatura>> getByCuatrimestre(Integer cuatrimestre);
    
    CompletableFuture<List<Asignatura>> getByPrograma(Integer programaId);
    
    CompletableFuture<List<Asignatura>> searchByName(String searchTerm);
    
    CompletableFuture<Void> update(Asignatura asignatura);
    
    CompletableFuture<Void> delete(Integer id);
    
    CompletableFuture<Boolean> existsByNombre(String nombre, Integer programaId);
    
    // Métodos para estadísticas
    CompletableFuture<Integer> countByPrograma(Integer programaId);
    
    CompletableFuture<Integer> countByCuatrimestre(Integer cuatrimestre);
}