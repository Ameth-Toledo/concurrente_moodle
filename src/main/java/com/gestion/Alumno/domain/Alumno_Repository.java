package com.gestion.Alumno.domain;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.Alumno.domain.entities.Alumno;

public interface Alumno_Repository {
    
    CompletableFuture<Alumno> save(Alumno alumno);
    
    CompletableFuture<List<Alumno>> getAll();
    
    CompletableFuture<Alumno> getById(Integer id);
    
    CompletableFuture<Alumno> getByMatricula(String matricula);
    
    CompletableFuture<List<Alumno>> getByCuatrimestre(Integer cuatrimestre);
    
    CompletableFuture<List<Alumno>> getByPrograma(Integer programaId);
    
    CompletableFuture<List<Alumno>> searchByName(String searchTerm);
    
    CompletableFuture<Void> update(Alumno alumno);
    
    CompletableFuture<Void> delete(Integer id);
    
    CompletableFuture<Boolean> existsByMatricula(String matricula);
    
    CompletableFuture<Boolean> existsByEmail(String email);
}