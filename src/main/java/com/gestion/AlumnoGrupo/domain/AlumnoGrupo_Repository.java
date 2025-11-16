package com.gestion.AlumnoGrupo.domain;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gestion.AlumnoGrupo.domain.entities.AlumnoGrupo;

public interface AlumnoGrupo_Repository {
    
    // Inscribir alumno a grupo
    CompletableFuture<AlumnoGrupo> inscribir(AlumnoGrupo alumnoGrupo);
    
    // Dar de baja alumno de grupo
    CompletableFuture<Void> desinscribir(Integer alumnoId, Integer grupoId);
    
    // Obtener todos los grupos de un alumno
    CompletableFuture<List<Integer>> getGruposByAlumno(Integer alumnoId);
    
    // Obtener todos los alumnos de un grupo
    CompletableFuture<List<Integer>> getAlumnosByGrupo(Integer grupoId);
    
    // Obtener todas las inscripciones
    CompletableFuture<List<AlumnoGrupo>> getAll();
    
    // Verificar si existe la inscripción
    CompletableFuture<Boolean> existsInscripcion(Integer alumnoId, Integer grupoId);
    
    // Contar alumnos en un grupo
    CompletableFuture<Integer> countAlumnosInGrupo(Integer grupoId);
    
    // Contar grupos de un alumno
    CompletableFuture<Integer> countGruposOfAlumno(Integer alumnoId);
    
    // Verificar capacidad del grupo
    CompletableFuture<Boolean> hasCapacity(Integer grupoId);
}