package src.main.java.com.gestion.Grupo.infrastructure.controllers;

import src.main.java.com.gestion.Grupo.application.CreateGrupo_UseCase;
import src.main.java.com.gestion.Grupo.domain.entities.Grupo;
import src.main.java.com.gestion.Grupo.domain.dto.GrupoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/grupos")
public class CreateGrupo_Controller {
    private final CreateGrupo_UseCase createGrupoUseCase;

    public CreateGrupo_Controller(CreateGrupo_UseCase createGrupoUseCase) {
        this.createGrupoUseCase = createGrupoUseCase;
    }

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
            .exceptionally(ex -> {
                Map<String, Object> error = new HashMap<>();
                
                if (ex.getCause() instanceof IllegalArgumentException) {
                    error.put("error", ex.getCause().getMessage());
                    return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(error);
                }
                
                error.put("error", "Error interno del servidor: " + ex.getMessage());
                return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(error);
            });
    }
}