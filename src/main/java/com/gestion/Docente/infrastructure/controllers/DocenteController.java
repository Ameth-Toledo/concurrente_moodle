package com.gestion.Docente.infrastructure.controllers;

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

import com.gestion.Docente.application.AssignAsignatura_UseCase;
import com.gestion.Docente.application.CreateDocente_UseCase;
import com.gestion.Docente.application.DeleteDocente_UseCase;
import com.gestion.Docente.application.GetAllDocentes_UseCase;
import com.gestion.Docente.application.GetAsignaturasByDocente_UseCase;
import com.gestion.Docente.application.GetDocenteById_UseCase;
import com.gestion.Docente.application.RemoveAsignatura_UseCase;
import com.gestion.Docente.application.SearchDocentes_UseCase;
import com.gestion.Docente.application.UpdateDocente_UseCase;
import com.gestion.Docente.domain.dto.DocenteResponse;
import com.gestion.Docente.domain.entities.Docente;

@RestController
@RequestMapping("/api/docentes")
public class DocenteController {
    
    private final CreateDocente_UseCase createDocenteUseCase;
    private final GetAllDocentes_UseCase getAllDocentesUseCase;
    private final GetDocenteById_UseCase getDocenteByIdUseCase;
    private final SearchDocentes_UseCase searchDocentesUseCase;
    private final UpdateDocente_UseCase updateDocenteUseCase;
    private final DeleteDocente_UseCase deleteDocenteUseCase;
    private final AssignAsignatura_UseCase assignAsignaturaUseCase;
    private final RemoveAsignatura_UseCase removeAsignaturaUseCase;
    private final GetAsignaturasByDocente_UseCase getAsignaturasByDocenteUseCase;

    public DocenteController(
            CreateDocente_UseCase createDocenteUseCase,
            GetAllDocentes_UseCase getAllDocentesUseCase,
            GetDocenteById_UseCase getDocenteByIdUseCase,
            SearchDocentes_UseCase searchDocentesUseCase,
            UpdateDocente_UseCase updateDocenteUseCase,
            DeleteDocente_UseCase deleteDocenteUseCase,
            AssignAsignatura_UseCase assignAsignaturaUseCase,
            RemoveAsignatura_UseCase removeAsignaturaUseCase,
            GetAsignaturasByDocente_UseCase getAsignaturasByDocenteUseCase) {
        this.createDocenteUseCase = createDocenteUseCase;
        this.getAllDocentesUseCase = getAllDocentesUseCase;
        this.getDocenteByIdUseCase = getDocenteByIdUseCase;
        this.searchDocentesUseCase = searchDocentesUseCase;
        this.updateDocenteUseCase = updateDocenteUseCase;
        this.deleteDocenteUseCase = deleteDocenteUseCase;
        this.assignAsignaturaUseCase = assignAsignaturaUseCase;
        this.removeAsignaturaUseCase = removeAsignaturaUseCase;
        this.getAsignaturasByDocenteUseCase = getAsignaturasByDocenteUseCase;
    }

    // ============================================
    // POST - Crear docente
    // ============================================
    @PostMapping
    public CompletableFuture<ResponseEntity<Map<String, Object>>> createDocente(@RequestBody Docente docente) {
        return createDocenteUseCase.execute(docente)
            .thenApply(createdDocente -> {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Docente creado exitosamente");
                response.put("docente", DocenteResponse.fromDocente(createdDocente));
                
                return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener todos los docentes
    // ============================================
    @GetMapping
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getAllDocentes() {
        return getAllDocentesUseCase.execute()
            .thenApply(docentes -> {
                List<DocenteResponse> docenteResponses = docentes.stream()
                    .map(DocenteResponse::fromDocente)
                    .collect(Collectors.toList());
                
                Map<String, Object> response = new HashMap<>();
                response.put("docentes", docenteResponses);
                response.put("total", docenteResponses.size());
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener docente por ID
    // ============================================
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getDocenteById(@PathVariable Integer id) {
        return getDocenteByIdUseCase.execute(id)
            .thenApply(docente -> {
                Map<String, Object> response = new HashMap<>();
                response.put("docente", DocenteResponse.fromDocente(docente));
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Buscar docentes
    // ============================================
    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> searchDocentes(
            @RequestParam String q) {
        return searchDocentesUseCase.execute(q)
            .thenApply(docentes -> {
                List<DocenteResponse> docenteResponses = docentes.stream()
                    .map(DocenteResponse::fromDocente)
                    .collect(Collectors.toList());
                
                Map<String, Object> response = new HashMap<>();
                response.put("searchTerm", q);
                response.put("docentes", docenteResponses);
                response.put("total", docenteResponses.size());
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // PUT - Actualizar docente
    // ============================================
    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> updateDocente(
            @PathVariable Integer id, 
            @RequestBody Docente docente) {
        return updateDocenteUseCase.execute(id, docente)
            .thenApply(updatedDocente -> {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Docente actualizado exitosamente");
                response.put("docente", DocenteResponse.fromDocente(updatedDocente));
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // DELETE - Eliminar docente
    // ============================================
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> deleteDocente(@PathVariable Integer id) {
        return deleteDocenteUseCase.execute(id)
            .thenApply(v -> {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Docente eliminado exitosamente");
                response.put("id", id);
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // POST - Asignar asignatura a docente
    // ============================================
    @PostMapping("/{docenteId}/asignaturas/{asignaturaId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> assignAsignatura(
            @PathVariable Integer docenteId,
            @PathVariable Integer asignaturaId) {
        return assignAsignaturaUseCase.execute(docenteId, asignaturaId)
            .thenApply(v -> {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Asignatura asignada exitosamente");
                response.put("docenteId", docenteId);
                response.put("asignaturaId", asignaturaId);
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // DELETE - Remover asignatura de docente
    // ============================================
    @DeleteMapping("/{docenteId}/asignaturas/{asignaturaId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> removeAsignatura(
            @PathVariable Integer docenteId,
            @PathVariable Integer asignaturaId) {
        return removeAsignaturaUseCase.execute(docenteId, asignaturaId)
            .thenApply(v -> {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Asignatura removida exitosamente");
                response.put("docenteId", docenteId);
                response.put("asignaturaId", asignaturaId);
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener asignaturas de un docente
    // ============================================
    @GetMapping("/{docenteId}/asignaturas")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getAsignaturasByDocente(
            @PathVariable Integer docenteId) {
        return getAsignaturasByDocenteUseCase.execute(docenteId)
            .thenApply(asignaturaIds -> {
                Map<String, Object> response = new HashMap<>();
                response.put("docenteId", docenteId);
                response.put("asignaturaIds", asignaturaIds);
                response.put("total", asignaturaIds.size());
                
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