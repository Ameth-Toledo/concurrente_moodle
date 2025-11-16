package com.gestion.AlumnoGrupo.infrastructure.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.AlumnoGrupo.application.DesinscribirAlumno_UseCase;
import com.gestion.AlumnoGrupo.application.GetAllInscripciones_UseCase;
import com.gestion.AlumnoGrupo.application.GetAlumnosByGrupo_UseCase;
import com.gestion.AlumnoGrupo.application.GetGruposByAlumno_UseCase;
import com.gestion.AlumnoGrupo.application.GetInscripcionStats_UseCase;
import com.gestion.AlumnoGrupo.application.InscribirAlumno_UseCase;
import com.gestion.AlumnoGrupo.domain.dto.AlumnoGrupoResponse;

@RestController
@RequestMapping("/api/inscripciones")
public class AlumnoGrupoController {
    
    private final InscribirAlumno_UseCase inscribirAlumnoUseCase;
    private final DesinscribirAlumno_UseCase desinscribirAlumnoUseCase;
    private final GetGruposByAlumno_UseCase getGruposByAlumnoUseCase;
    private final GetAlumnosByGrupo_UseCase getAlumnosByGrupoUseCase;
    private final GetAllInscripciones_UseCase getAllInscripcionesUseCase;
    private final GetInscripcionStats_UseCase getInscripcionStatsUseCase;

    public AlumnoGrupoController(
            InscribirAlumno_UseCase inscribirAlumnoUseCase,
            DesinscribirAlumno_UseCase desinscribirAlumnoUseCase,
            GetGruposByAlumno_UseCase getGruposByAlumnoUseCase,
            GetAlumnosByGrupo_UseCase getAlumnosByGrupoUseCase,
            GetAllInscripciones_UseCase getAllInscripcionesUseCase,
            GetInscripcionStats_UseCase getInscripcionStatsUseCase) {
        this.inscribirAlumnoUseCase = inscribirAlumnoUseCase;
        this.desinscribirAlumnoUseCase = desinscribirAlumnoUseCase;
        this.getGruposByAlumnoUseCase = getGruposByAlumnoUseCase;
        this.getAlumnosByGrupoUseCase = getAlumnosByGrupoUseCase;
        this.getAllInscripcionesUseCase = getAllInscripcionesUseCase;
        this.getInscripcionStatsUseCase = getInscripcionStatsUseCase;
    }

    // ============================================
    // POST - Inscribir alumno a grupo
    // ============================================
    @PostMapping("/alumnos/{alumnoId}/grupos/{grupoId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> inscribirAlumno(
            @PathVariable Integer alumnoId,
            @PathVariable Integer grupoId) {
        return inscribirAlumnoUseCase.execute(alumnoId, grupoId)
            .thenApply(inscripcion -> {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Alumno inscrito exitosamente");
                response.put("inscripcion", AlumnoGrupoResponse.fromAlumnoGrupo(inscripcion));
                
                return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // DELETE - Desinscribir alumno de grupo
    // ============================================
    @DeleteMapping("/alumnos/{alumnoId}/grupos/{grupoId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> desinscribirAlumno(
            @PathVariable Integer alumnoId,
            @PathVariable Integer grupoId) {
        return desinscribirAlumnoUseCase.execute(alumnoId, grupoId)
            .thenApply(v -> {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Alumno desinscrito exitosamente");
                response.put("alumnoId", alumnoId);
                response.put("grupoId", grupoId);
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener todas las inscripciones
    // ============================================
    @GetMapping
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getAllInscripciones() {
        return getAllInscripcionesUseCase.execute()
            .thenApply(inscripciones -> {
                List<AlumnoGrupoResponse> inscripcionResponses = inscripciones.stream()
                    .map(AlumnoGrupoResponse::fromAlumnoGrupo)
                    .collect(Collectors.toList());
                
                Map<String, Object> response = new HashMap<>();
                response.put("inscripciones", inscripcionResponses);
                response.put("total", inscripcionResponses.size());
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener grupos de un alumno
    // ============================================
    @GetMapping("/alumnos/{alumnoId}/grupos")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getGruposByAlumno(
            @PathVariable Integer alumnoId) {
        return getGruposByAlumnoUseCase.execute(alumnoId)
            .thenApply(grupoIds -> {
                Map<String, Object> response = new HashMap<>();
                response.put("alumnoId", alumnoId);
                response.put("grupoIds", grupoIds);
                response.put("total", grupoIds.size());
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener alumnos de un grupo
    // ============================================
    @GetMapping("/grupos/{grupoId}/alumnos")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getAlumnosByGrupo(
            @PathVariable Integer grupoId) {
        return getAlumnosByGrupoUseCase.execute(grupoId)
            .thenApply(alumnoIds -> {
                Map<String, Object> response = new HashMap<>();
                response.put("grupoId", grupoId);
                response.put("alumnoIds", alumnoIds);
                response.put("total", alumnoIds.size());
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Estadísticas de un grupo
    // ============================================
    @GetMapping("/grupos/{grupoId}/stats")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getGrupoStats(
            @PathVariable Integer grupoId) {
        return getInscripcionStatsUseCase.getGrupoStats(grupoId)
            .thenApply(stats -> {
                Map<String, Object> response = new HashMap<>();
                response.put("stats", stats);
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Estadísticas de un alumno
    // ============================================
    @GetMapping("/alumnos/{alumnoId}/stats")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getAlumnoStats(
            @PathVariable Integer alumnoId) {
        return getInscripcionStatsUseCase.getAlumnoStats(alumnoId)
            .thenApply(stats -> {
                Map<String, Object> response = new HashMap<>();
                response.put("stats", stats);
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // Manejador de excepciones
    // ============================================
    private ResponseEntity<Map<String, Object>> handleException(Throwable ex) {
        Map<String, Object> error = new HashMap<>();
        
        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
        
        if (cause instanceof IllegalArgumentException) {
            error.put("error", cause.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
        }
        
        if (cause instanceof RuntimeException && cause.getMessage().contains("no encontrad")) {
            error.put("error", cause.getMessage());
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
        }
        
        error.put("error", "Error interno del servidor: " + cause.getMessage());
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(error);
    }
}