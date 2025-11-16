package com.gestion.Asignatura.infrastructure.controllers;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.Asignatura.application.CreateAsignatura_UseCase;
import com.gestion.Asignatura.application.DeleteAsignatura_UseCase;
import com.gestion.Asignatura.application.GetAllAsignaturas_UseCase;
import com.gestion.Asignatura.application.GetAsignaturaById_UseCase;
import com.gestion.Asignatura.application.GetAsignaturasByCuatrimestre_UseCase;
import com.gestion.Asignatura.application.GetAsignaturasByPrograma_UseCase;
import com.gestion.Asignatura.application.SearchAsignaturas_UseCase;
import com.gestion.Asignatura.application.UpdateAsignatura_UseCase;
import com.gestion.Asignatura.domain.dto.AsignaturaResponse;
import com.gestion.Asignatura.domain.entities.Asignatura;

@RestController
@RequestMapping("/api/asignaturas")
public class AsignaturaController {
    
    private final CreateAsignatura_UseCase createAsignaturaUseCase;
    private final GetAllAsignaturas_UseCase getAllAsignaturasUseCase;
    private final GetAsignaturaById_UseCase getAsignaturaByIdUseCase;
    private final GetAsignaturasByCuatrimestre_UseCase getAsignaturasByCuatrimestreUseCase;
    private final GetAsignaturasByPrograma_UseCase getAsignaturasByProgramaUseCase;
    private final SearchAsignaturas_UseCase searchAsignaturasUseCase;
    private final UpdateAsignatura_UseCase updateAsignaturaUseCase;
    private final DeleteAsignatura_UseCase deleteAsignaturaUseCase;

    public AsignaturaController(
            CreateAsignatura_UseCase createAsignaturaUseCase,
            GetAllAsignaturas_UseCase getAllAsignaturasUseCase,
            GetAsignaturaById_UseCase getAsignaturaByIdUseCase,
            GetAsignaturasByCuatrimestre_UseCase getAsignaturasByCuatrimestreUseCase,
            GetAsignaturasByPrograma_UseCase getAsignaturasByProgramaUseCase,
            SearchAsignaturas_UseCase searchAsignaturasUseCase,
            UpdateAsignatura_UseCase updateAsignaturaUseCase,
            DeleteAsignatura_UseCase deleteAsignaturaUseCase) {
        this.createAsignaturaUseCase = createAsignaturaUseCase;
        this.getAllAsignaturasUseCase = getAllAsignaturasUseCase;
        this.getAsignaturaByIdUseCase = getAsignaturaByIdUseCase;
        this.getAsignaturasByCuatrimestreUseCase = getAsignaturasByCuatrimestreUseCase;
        this.getAsignaturasByProgramaUseCase = getAsignaturasByProgramaUseCase;
        this.searchAsignaturasUseCase = searchAsignaturasUseCase;
        this.updateAsignaturaUseCase = updateAsignaturaUseCase;
        this.deleteAsignaturaUseCase = deleteAsignaturaUseCase;
    }

    // ============================================
    // POST - Crear asignatura
    // ============================================
    @PostMapping
    public CompletableFuture<ResponseEntity<Map<String, Object>>> createAsignatura(@RequestBody Asignatura asignatura) {
        return createAsignaturaUseCase.execute(asignatura)
            .thenApply(createdAsignatura -> {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Asignatura creada exitosamente");
                response.put("asignatura", AsignaturaResponse.fromAsignatura(createdAsignatura));
                
                return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener todas las asignaturas
    // ============================================
    @GetMapping
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getAllAsignaturas() {
        return getAllAsignaturasUseCase.execute()
            .thenApply(asignaturas -> {
                List<AsignaturaResponse> asignaturaResponses = asignaturas.stream()
                    .map(AsignaturaResponse::fromAsignatura)
                    .collect(Collectors.toList());
                
                Map<String, Object> response = new HashMap<>();
                response.put("asignaturas", asignaturaResponses);
                response.put("total", asignaturaResponses.size());
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener asignatura por ID
    // ============================================
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getAsignaturaById(@PathVariable Integer id) {
        return getAsignaturaByIdUseCase.execute(id)
            .thenApply(asignatura -> {
                Map<String, Object> response = new HashMap<>();
                response.put("asignatura", AsignaturaResponse.fromAsignatura(asignatura));
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener asignaturas por cuatrimestre
    // ============================================
    @GetMapping("/cuatrimestre/{cuatrimestre}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getAsignaturasByCuatrimestre(
            @PathVariable Integer cuatrimestre) {
        return getAsignaturasByCuatrimestreUseCase.execute(cuatrimestre)
            .thenApply(asignaturas -> {
                List<AsignaturaResponse> asignaturaResponses = asignaturas.stream()
                    .map(AsignaturaResponse::fromAsignatura)
                    .collect(Collectors.toList());
                
                Map<String, Object> response = new HashMap<>();
                response.put("cuatrimestre", cuatrimestre);
                response.put("asignaturas", asignaturaResponses);
                response.put("total", asignaturaResponses.size());
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener asignaturas por programa
    // ============================================
    @GetMapping("/programa/{programaId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getAsignaturasByPrograma(
            @PathVariable Integer programaId) {
        return getAsignaturasByProgramaUseCase.execute(programaId)
            .thenApply(asignaturas -> {
                List<AsignaturaResponse> asignaturaResponses = asignaturas.stream()
                    .map(AsignaturaResponse::fromAsignatura)
                    .collect(Collectors.toList());
                
                Map<String, Object> response = new HashMap<>();
                response.put("programaId", programaId);
                response.put("asignaturas", asignaturaResponses);
                response.put("total", asignaturaResponses.size());
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Buscar asignaturas
    // ============================================
    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> searchAsignaturas(
            @RequestParam String q) {
        return searchAsignaturasUseCase.execute(q)
            .thenApply(asignaturas -> {
                List<AsignaturaResponse> asignaturaResponses = asignaturas.stream()
                    .map(AsignaturaResponse::fromAsignatura)
                    .collect(Collectors.toList());
                
                Map<String, Object> response = new HashMap<>();
                response.put("searchTerm", q);
                response.put("asignaturas", asignaturaResponses);
                response.put("total", asignaturaResponses.size());
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // PUT - Actualizar asignatura
    // ============================================
    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> updateAsignatura(
            @PathVariable Integer id, 
            @RequestBody Asignatura asignatura) {
        return updateAsignaturaUseCase.execute(id, asignatura)
            .thenApply(updatedAsignatura -> {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Asignatura actualizada exitosamente");
                response.put("asignatura", AsignaturaResponse.fromAsignatura(updatedAsignatura));
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // DELETE - Eliminar asignatura
    // ============================================
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> deleteAsignatura(@PathVariable Integer id) {
        return deleteAsignaturaUseCase.execute(id)
            .thenApply(v -> {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Asignatura eliminada exitosamente");
                response.put("id", id);
                
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