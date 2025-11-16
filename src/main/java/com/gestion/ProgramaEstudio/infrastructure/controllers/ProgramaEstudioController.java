package com.gestion.ProgramaEstudio.infrastructure.controllers;

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

import com.gestion.ProgramaEstudio.application.CreateProgramaEstudio_UseCase;
import com.gestion.ProgramaEstudio.application.DeleteProgramaEstudio_UseCase;
import com.gestion.ProgramaEstudio.application.GetAllProgramasEstudio_UseCase;
import com.gestion.ProgramaEstudio.application.GetProgramaEstudioById_UseCase;
import com.gestion.ProgramaEstudio.application.GetProgramaEstudioStats_UseCase;
import com.gestion.ProgramaEstudio.application.SearchProgramasEstudio_UseCase;
import com.gestion.ProgramaEstudio.application.UpdateProgramaEstudio_UseCase;
import com.gestion.ProgramaEstudio.domain.dto.ProgramaEstudioResponse;
import com.gestion.ProgramaEstudio.domain.entities.ProgramaEstudio;

@RestController
@RequestMapping("/api/programas")
public class ProgramaEstudioController {
    
    private final CreateProgramaEstudio_UseCase createProgramaEstudioUseCase;
    private final GetAllProgramasEstudio_UseCase getAllProgramasEstudioUseCase;
    private final GetProgramaEstudioById_UseCase getProgramaEstudioByIdUseCase;
    private final SearchProgramasEstudio_UseCase searchProgramasEstudioUseCase;
    private final UpdateProgramaEstudio_UseCase updateProgramaEstudioUseCase;
    private final DeleteProgramaEstudio_UseCase deleteProgramaEstudioUseCase;
    private final GetProgramaEstudioStats_UseCase getProgramaEstudioStatsUseCase;

    public ProgramaEstudioController(
            CreateProgramaEstudio_UseCase createProgramaEstudioUseCase,
            GetAllProgramasEstudio_UseCase getAllProgramasEstudioUseCase,
            GetProgramaEstudioById_UseCase getProgramaEstudioByIdUseCase,
            SearchProgramasEstudio_UseCase searchProgramasEstudioUseCase,
            UpdateProgramaEstudio_UseCase updateProgramaEstudioUseCase,
            DeleteProgramaEstudio_UseCase deleteProgramaEstudioUseCase,
            GetProgramaEstudioStats_UseCase getProgramaEstudioStatsUseCase) {
        this.createProgramaEstudioUseCase = createProgramaEstudioUseCase;
        this.getAllProgramasEstudioUseCase = getAllProgramasEstudioUseCase;
        this.getProgramaEstudioByIdUseCase = getProgramaEstudioByIdUseCase;
        this.searchProgramasEstudioUseCase = searchProgramasEstudioUseCase;
        this.updateProgramaEstudioUseCase = updateProgramaEstudioUseCase;
        this.deleteProgramaEstudioUseCase = deleteProgramaEstudioUseCase;
        this.getProgramaEstudioStatsUseCase = getProgramaEstudioStatsUseCase;
    }

    // ============================================
    // POST - Crear programa de estudio
    // ============================================
    @PostMapping
    public CompletableFuture<ResponseEntity<Map<String, Object>>> createProgramaEstudio(
            @RequestBody ProgramaEstudio programaEstudio) {
        return createProgramaEstudioUseCase.execute(programaEstudio)
            .thenApply(createdPrograma -> {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Programa de estudio creado exitosamente");
                response.put("programa", ProgramaEstudioResponse.fromProgramaEstudio(createdPrograma));
                
                return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener todos los programas de estudio
    // ============================================
    @GetMapping
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getAllProgramasEstudio() {
        return getAllProgramasEstudioUseCase.execute()
            .thenApply(programas -> {
                List<ProgramaEstudioResponse> programaResponses = programas.stream()
                    .map(ProgramaEstudioResponse::fromProgramaEstudio)
                    .collect(Collectors.toList());
                
                Map<String, Object> response = new HashMap<>();
                response.put("programas", programaResponses);
                response.put("total", programaResponses.size());
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener programa de estudio por ID
    // ============================================
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getProgramaEstudioById(
            @PathVariable Integer id) {
        return getProgramaEstudioByIdUseCase.execute(id)
            .thenApply(programa -> {
                Map<String, Object> response = new HashMap<>();
                response.put("programa", ProgramaEstudioResponse.fromProgramaEstudio(programa));
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener estadísticas del programa
    // ============================================
    @GetMapping("/{id}/stats")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getProgramaEstudioStats(
            @PathVariable Integer id) {
        return getProgramaEstudioStatsUseCase.execute(id)
            .thenApply(stats -> {
                Map<String, Object> response = new HashMap<>();
                response.put("stats", stats);
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Buscar programas de estudio
    // ============================================
    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> searchProgramasEstudio(
            @RequestParam String q) {
        return searchProgramasEstudioUseCase.execute(q)
            .thenApply(programas -> {
                List<ProgramaEstudioResponse> programaResponses = programas.stream()
                    .map(ProgramaEstudioResponse::fromProgramaEstudio)
                    .collect(Collectors.toList());
                
                Map<String, Object> response = new HashMap<>();
                response.put("searchTerm", q);
                response.put("programas", programaResponses);
                response.put("total", programaResponses.size());
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // PUT - Actualizar programa de estudio
    // ============================================
    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> updateProgramaEstudio(
            @PathVariable Integer id, 
            @RequestBody ProgramaEstudio programaEstudio) {
        return updateProgramaEstudioUseCase.execute(id, programaEstudio)
            .thenApply(updatedPrograma -> {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Programa de estudio actualizado exitosamente");
                response.put("programa", ProgramaEstudioResponse.fromProgramaEstudio(updatedPrograma));
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // DELETE - Eliminar programa de estudio
    // ============================================
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> deleteProgramaEstudio(
            @PathVariable Integer id) {
        return deleteProgramaEstudioUseCase.execute(id)
            .thenApply(v -> {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Programa de estudio eliminado exitosamente");
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