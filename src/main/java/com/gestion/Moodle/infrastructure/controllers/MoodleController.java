package com.gestion.Moodle.infrastructure.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.Moodle.application.MoodleSyncService;
import com.gestion.Moodle.config.MoodleConfig;
import com.gestion.Moodle.infrastructure.client.MoodleClient;

@RestController
@RequestMapping("/api/moodle")
public class MoodleController {
    
    private final MoodleClient moodleClient;
    private final MoodleSyncService moodleSyncService;
    private final MoodleConfig moodleConfig;

    public MoodleController(MoodleClient moodleClient, MoodleSyncService moodleSyncService) {
        this.moodleClient = moodleClient;
        this.moodleSyncService = moodleSyncService;
        this.moodleConfig = MoodleConfig.getInstance();
    }

    // ============================================
    // GET - Verificar configuración
    // ============================================
    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> getConfig() {
        Map<String, Object> response = new HashMap<>();
        response.put("configured", moodleConfig.isConfigured());
        response.put("moodleUrl", moodleConfig.getMoodleUrl());
        response.put("webServiceUrl", moodleConfig.getWebServiceUrl());
        
        return ResponseEntity.ok(response);
    }

    // ============================================
    // GET - Test de conexión
    // ============================================
    @GetMapping("/test")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> testConnection() {
        return moodleClient.testConnection()
            .thenCompose(isConnected -> {
                if (isConnected) {
                    return moodleClient.getSiteInfo()
                        .thenApply(siteInfo -> {
                            Map<String, Object> response = new HashMap<>();
                            response.put("connected", true);
                            response.put("sitename", siteInfo.get("sitename").asText());
                            response.put("siteurl", siteInfo.get("siteurl").asText());
                            response.put("moodleversion", siteInfo.has("release") ? 
                                siteInfo.get("release").asText() : "Unknown");
                            return ResponseEntity.ok(response);
                        });
                } else {
                    Map<String, Object> response = new HashMap<>();
                    response.put("connected", false);
                    response.put("message", "No se pudo conectar con Moodle");
                    return CompletableFuture.completedFuture(
                        ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response)
                    );
                }
            })
            .exceptionally(ex -> {
                Map<String, Object> error = new HashMap<>();
                error.put("connected", false);
                error.put("error", ex.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            });
    }

    // ============================================
    // POST - Sincronizar alumno
    // ============================================
    @PostMapping("/sync/alumno/{alumnoId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> syncAlumno(
            @PathVariable Integer alumnoId) {
        return moodleSyncService.syncAlumno(alumnoId)
            .thenApply(result -> ResponseEntity.ok(result))
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // POST - Sincronizar docente
    // ============================================
    @PostMapping("/sync/docente/{docenteId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> syncDocente(
            @PathVariable Integer docenteId) {
        return moodleSyncService.syncDocente(docenteId)
            .thenApply(result -> ResponseEntity.ok(result))
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // POST - Sincronizar asignatura como curso
    // ============================================
    @PostMapping("/sync/asignatura/{asignaturaId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> syncAsignatura(
            @PathVariable Integer asignaturaId) {
        return moodleSyncService.syncAsignatura(asignaturaId)
            .thenApply(result -> ResponseEntity.ok(result))
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // POST - Matricular alumno en curso
    // ============================================
    @PostMapping("/enrol/alumno/{alumnoId}/course/{courseId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> enrolAlumno(
            @PathVariable Integer alumnoId,
            @PathVariable Integer courseId) {
        return moodleSyncService.enrolAlumnoToCourse(alumnoId, courseId)
            .thenApply(result -> ResponseEntity.ok(result))
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // POST - Asignar docente a curso
    // ============================================
    @PostMapping("/enrol/docente/{docenteId}/course/{courseId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> assignDocente(
            @PathVariable Integer docenteId,
            @PathVariable Integer courseId) {
        return moodleSyncService.assignDocenteToCourse(docenteId, courseId)
            .thenApply(result -> ResponseEntity.ok(result))
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // POST - Sincronizar todos los alumnos
    // ============================================
    @PostMapping("/sync/alumnos/all")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> syncAllAlumnos() {
        return moodleSyncService.syncAllAlumnos()
            .thenApply(result -> ResponseEntity.ok(result))
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // POST - Ejecutar función personalizada de Moodle
    // ============================================
    @PostMapping("/execute")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> executeMoodleFunction(
            @RequestBody Map<String, Object> request) {
        
        String functionName = (String) request.get("function");
        @SuppressWarnings("unchecked")
        Map<String, String> params = (Map<String, String>) request.get("params");
        
        if (functionName == null || functionName.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "El nombre de la función es obligatorio");
            return CompletableFuture.completedFuture(
                ResponseEntity.badRequest().body(error)
            );
        }

        return moodleClient.executeFunction(functionName, params)
            .thenApply(result -> {
                Map<String, Object> response = new HashMap<>();
                response.put("function", functionName);
                response.put("result", result);
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
        
        error.put("error", "Error: " + cause.getMessage());
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(error);
    }
}