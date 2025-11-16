package com.gestion.Alumno.infrastructure.controllers;

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

import com.gestion.Alumno.application.CreateAlumno_UseCase;
import com.gestion.Alumno.application.DeleteAlumno_UseCase;
import com.gestion.Alumno.application.GetAllAlumnos_UseCase;
import com.gestion.Alumno.application.GetAlumnoById_UseCase;
import com.gestion.Alumno.application.GetAlumnoByMatricula_UseCase;
import com.gestion.Alumno.application.GetAlumnosByCuatrimestre_UseCase;
import com.gestion.Alumno.application.SearchAlumnos_UseCase;
import com.gestion.Alumno.application.UpdateAlumno_UseCase;
import com.gestion.Alumno.domain.dto.AlumnoResponse;
import com.gestion.Alumno.domain.entities.Alumno;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {
    
    private final CreateAlumno_UseCase createAlumnoUseCase;
    private final GetAllAlumnos_UseCase getAllAlumnosUseCase;
    private final GetAlumnoById_UseCase getAlumnoByIdUseCase;
    private final GetAlumnoByMatricula_UseCase getAlumnoByMatriculaUseCase;
    private final GetAlumnosByCuatrimestre_UseCase getAlumnosByCuatrimestreUseCase;
    private final SearchAlumnos_UseCase searchAlumnosUseCase;
    private final UpdateAlumno_UseCase updateAlumnoUseCase;
    private final DeleteAlumno_UseCase deleteAlumnoUseCase;

    public AlumnoController(
            CreateAlumno_UseCase createAlumnoUseCase,
            GetAllAlumnos_UseCase getAllAlumnosUseCase,
            GetAlumnoById_UseCase getAlumnoByIdUseCase,
            GetAlumnoByMatricula_UseCase getAlumnoByMatriculaUseCase,
            GetAlumnosByCuatrimestre_UseCase getAlumnosByCuatrimestreUseCase,
            SearchAlumnos_UseCase searchAlumnosUseCase,
            UpdateAlumno_UseCase updateAlumnoUseCase,
            DeleteAlumno_UseCase deleteAlumnoUseCase) {
        this.createAlumnoUseCase = createAlumnoUseCase;
        this.getAllAlumnosUseCase = getAllAlumnosUseCase;
        this.getAlumnoByIdUseCase = getAlumnoByIdUseCase;
        this.getAlumnoByMatriculaUseCase = getAlumnoByMatriculaUseCase;
        this.getAlumnosByCuatrimestreUseCase = getAlumnosByCuatrimestreUseCase;
        this.searchAlumnosUseCase = searchAlumnosUseCase;
        this.updateAlumnoUseCase = updateAlumnoUseCase;
        this.deleteAlumnoUseCase = deleteAlumnoUseCase;
    }

    // ============================================
    // POST - Crear alumno
    // ============================================
    @PostMapping
    public CompletableFuture<ResponseEntity<Map<String, Object>>> createAlumno(@RequestBody Alumno alumno) {
        return createAlumnoUseCase.execute(alumno)
            .thenApply(createdAlumno -> {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Alumno creado exitosamente");
                response.put("alumno", AlumnoResponse.fromAlumno(createdAlumno));
                
                return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener todos los alumnos
    // ============================================
    @GetMapping
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getAllAlumnos() {
        return getAllAlumnosUseCase.execute()
            .thenApply(alumnos -> {
                List<AlumnoResponse> alumnoResponses = alumnos.stream()
                    .map(AlumnoResponse::fromAlumno)
                    .collect(Collectors.toList());
                
                Map<String, Object> response = new HashMap<>();
                response.put("alumnos", alumnoResponses);
                response.put("total", alumnoResponses.size());
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener alumno por ID
    // ============================================
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getAlumnoById(@PathVariable Integer id) {
        return getAlumnoByIdUseCase.execute(id)
            .thenApply(alumno -> {
                Map<String, Object> response = new HashMap<>();
                response.put("alumno", AlumnoResponse.fromAlumno(alumno));
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener alumno por matrícula
    // ============================================
    @GetMapping("/matricula/{matricula}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getAlumnoByMatricula(
            @PathVariable String matricula) {
        return getAlumnoByMatriculaUseCase.execute(matricula)
            .thenApply(alumno -> {
                Map<String, Object> response = new HashMap<>();
                response.put("alumno", AlumnoResponse.fromAlumno(alumno));
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Obtener alumnos por cuatrimestre
    // ============================================
    @GetMapping("/cuatrimestre/{cuatrimestre}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getAlumnosByCuatrimestre(
            @PathVariable Integer cuatrimestre) {
        return getAlumnosByCuatrimestreUseCase.execute(cuatrimestre)
            .thenApply(alumnos -> {
                List<AlumnoResponse> alumnoResponses = alumnos.stream()
                    .map(AlumnoResponse::fromAlumno)
                    .collect(Collectors.toList());
                
                Map<String, Object> response = new HashMap<>();
                response.put("cuatrimestre", cuatrimestre);
                response.put("alumnos", alumnoResponses);
                response.put("total", alumnoResponses.size());
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // GET - Buscar alumnos
    // ============================================
    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> searchAlumnos(
            @RequestParam String q) {
        return searchAlumnosUseCase.execute(q)
            .thenApply(alumnos -> {
                List<AlumnoResponse> alumnoResponses = alumnos.stream()
                    .map(AlumnoResponse::fromAlumno)
                    .collect(Collectors.toList());
                
                Map<String, Object> response = new HashMap<>();
                response.put("searchTerm", q);
                response.put("alumnos", alumnoResponses);
                response.put("total", alumnoResponses.size());
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // PUT - Actualizar alumno
    // ============================================
    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> updateAlumno(
            @PathVariable Integer id, 
            @RequestBody Alumno alumno) {
        return updateAlumnoUseCase.execute(id, alumno)
            .thenApply(updatedAlumno -> {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Alumno actualizado exitosamente");
                response.put("alumno", AlumnoResponse.fromAlumno(updatedAlumno));
                
                return ResponseEntity.ok(response);
            })
            .exceptionally(ex -> handleException(ex));
    }

    // ============================================
    // DELETE - Eliminar alumno
    // ============================================
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> deleteAlumno(@PathVariable Integer id) {
        return deleteAlumnoUseCase.execute(id)
            .thenApply(v -> {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Alumno eliminado exitosamente");
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