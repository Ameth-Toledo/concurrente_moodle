package com.gestion.Grupo.infrastructure.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import com.gestion.Grupo.application.CreateGrupo_UseCase;
import com.gestion.Grupo.application.DeleteGrupo_UseCase;
import com.gestion.Grupo.application.GetAllGrupos_UseCase;
import com.gestion.Grupo.application.GetGrupoById_UseCase;
import com.gestion.Grupo.application.GetGruposByAsignatura_UseCase;
import com.gestion.Grupo.application.GetGruposByCuatrimestre_UseCase;
import com.gestion.Grupo.application.GetGruposByDocente_UseCase;
import com.gestion.Grupo.application.UpdateGrupo_UseCase;
import com.gestion.Grupo.domain.dto.GrupoResponse;
import com.gestion.Grupo.domain.entities.Grupo;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController {
    
    private final CreateGrupo_UseCase createGrupoUseCase;
    private final GetAllGrupos_UseCase getAllGruposUseCase;
    private final GetGrupoById_UseCase getGrupoByIdUseCase;
    private final GetGruposByCuatrimestre_UseCase getGruposByCuatrimestreUseCase;
    private final GetGruposByDocente_UseCase getGruposByDocenteUseCase;
    private final GetGruposByAsignatura_UseCase getGruposByAsignaturaUseCase;
    private final UpdateGrupo_UseCase updateGrupoUseCase;
    private final DeleteGrupo_UseCase deleteGrupoUseCase;

    public GrupoController(
            CreateGrupo_UseCase createGrupoUseCase,
            GetAllGrupos_UseCase getAllGruposUseCase,
            GetGrupoById_UseCase getGrupoByIdUseCase,
            GetGruposByCuatrimestre_UseCase getGruposByCuatrimestreUseCase,
            GetGruposByDocente_UseCase getGruposByDocenteUseCase,
            GetGruposByAsignatura_UseCase getGruposByAsignaturaUseCase,
            UpdateGrupo_UseCase updateGrupoUseCase,
            DeleteGrupo_UseCase deleteGrupoUseCase) {
        this.createGrupoUseCase = createGrupoUseCase;
        this.getAllGruposUseCase = getAllGruposUseCase;
        this.getGrupoByIdUseCase = getGrupoByIdUseCase;
        this.getGruposByCuatrimestreUseCase = getGruposByCuatrimestreUseCase;
        this.getGruposByDocenteUseCase = getGruposByDocenteUseCase;
        this.getGruposByAsignaturaUseCase = getGruposByAsignaturaUseCase;
        this.updateGrupoUseCase = updateGrupoUseCase;
        this.deleteGrupoUseCase = deleteGrupoUseCase;
    }

    // ============================================
    // POST - Crear grupo
    // ============================================
    @PostMapping
    public CompletableFuture<ResponseEntity<Map<String, Object>>> createGrupo(@RequestBody Grupo grupo) {
        return createGrupoUseCase.execute(grupo)
            .thenApply(createdGrupo -> {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Grupo creado exitosamente");
                response.put("grupo", GrupoResponse.fromGrupo(createdGrupo));
                
                return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener todos los grupos
    // ============================================
    @GetMapping
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getAllGrupos() {
        return getAllGruposUseCase.execute()
            .thenApply(grupos -> {
                List<GrupoResponse> grupoResponses = grupos.stream()
                    .map(GrupoResponse::fromGrupo)
                    .collect(Collectors.toList());
                
                Map<String, Object> response = new HashMap<>();
                response.put("grupos", grupoResponses);
                response.put("total", grupoResponses.size());
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener grupo por ID
    // ============================================
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getGrupoById(@PathVariable Integer id) {
        return getGrupoByIdUseCase.execute(id)
            .thenApply(grupo -> {
                Map<String, Object> response = new HashMap<>();
                response.put("grupo", GrupoResponse.fromGrupo(grupo));
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener grupos por cuatrimestre
    // ============================================
    @GetMapping("/cuatrimestre/{cuatrimestre}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getGruposByCuatrimestre(
            @PathVariable Integer cuatrimestre) {
        return getGruposByCuatrimestreUseCase.execute(cuatrimestre)
            .thenApply(grupos -> {
                List<GrupoResponse> grupoResponses = grupos.stream()
                    .map(GrupoResponse::fromGrupo)
                    .collect(Collectors.toList());
                
                Map<String, Object> response = new HashMap<>();
                response.put("cuatrimestre", cuatrimestre);
                response.put("grupos", grupoResponses);
                response.put("total", grupoResponses.size());
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener grupos por docente
    // ============================================
    @GetMapping("/docente/{docenteId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getGruposByDocente(
            @PathVariable Integer docenteId) {
        return getGruposByDocenteUseCase.execute(docenteId)
            .thenApply(grupos -> {
                List<GrupoResponse> grupoResponses = grupos.stream()
                    .map(GrupoResponse::fromGrupo)
                    .collect(Collectors.toList());
                
                Map<String, Object> response = new HashMap<>();
                response.put("docenteId", docenteId);
                response.put("grupos", grupoResponses);
                response.put("total", grupoResponses.size());
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener grupos por asignatura
    // ============================================
    @GetMapping("/asignatura/{asignaturaId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getGruposByAsignatura(
            @PathVariable Integer asignaturaId) {
        return getGruposByAsignaturaUseCase.execute(asignaturaId)
            .thenApply(grupos -> {
                List<GrupoResponse> grupoResponses = grupos.stream()
                    .map(GrupoResponse::fromGrupo)
                    .collect(Collectors.toList());
                
                Map<String, Object> response = new HashMap<>();
                response.put("asignaturaId", asignaturaId);
                response.put("grupos", grupoResponses);
                response.put("total", grupoResponses.size());
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // PUT - Actualizar grupo
    // ============================================
    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> updateGrupo(
            @PathVariable Integer id, 
            @RequestBody Grupo grupo) {
        return updateGrupoUseCase.execute(id, grupo)
            .thenApply(updatedGrupo -> {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Grupo actualizado exitosamente");
                response.put("grupo", GrupoResponse.fromGrupo(updatedGrupo));
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // DELETE - Eliminar grupo
    // ============================================
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> deleteGrupo(@PathVariable Integer id) {
        return deleteGrupoUseCase.execute(id)
            .thenApply(v -> {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Grupo eliminado exitosamente");
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
        
        if (cause instanceof RuntimeException && cause.getMessage().contains("no encontrado")) {
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