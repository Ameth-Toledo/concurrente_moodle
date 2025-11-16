package com.gestion.Docente.domain;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.Docente.domain.entities.Docente;

public interface Docente_Repository {
    
    CompletableFuture<Docente> save(Docente docente);
    
    CompletableFuture<List<Docente>> getAll();
    
    CompletableFuture<Docente> getById(Integer id);
    
    CompletableFuture<Docente> getByEmail(String email);
    
    CompletableFuture<List<Docente>> searchByName(String searchTerm);
    
    CompletableFuture<Void> update(Docente docente);
    
    CompletableFuture<Void> delete(Integer id);
    
    CompletableFuture<Boolean> existsByEmail(String email);
    
    // Métodos específicos para relación con asignaturas
    CompletableFuture<Void> assignAsignatura(Integer docenteId, Integer asignaturaId);
    
    CompletableFuture<Void> removeAsignatura(Integer docenteId, Integer asignaturaId);
    
    CompletableFuture<List<Integer>> getAsignaturasByDocente(Integer docenteId);
}