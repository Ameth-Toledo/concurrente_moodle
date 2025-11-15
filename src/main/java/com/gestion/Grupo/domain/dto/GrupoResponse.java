package com.gestion.Grupo.domain.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.gestion.Grupo.domain.entities.Grupo;

public class GrupoResponse {
    private Integer id;
    private String nombre;
    private Integer asignaturaId;
    private Integer docenteId;
    private Integer cuatrimestre;
    private Integer capacidadMaxima;

    public GrupoResponse() {}

    public GrupoResponse(Integer id, String nombre, Integer asignaturaId, Integer docenteId, 
                        Integer cuatrimestre, Integer capacidadMaxima) {
        this.id = id;
        this.nombre = nombre;
        this.asignaturaId = asignaturaId;
        this.docenteId = docenteId;
        this.cuatrimestre = cuatrimestre;
        this.capacidadMaxima = capacidadMaxima;
    }

    public static GrupoResponse fromGrupo(Grupo grupo) {
        return new GrupoResponse(
            grupo.getId(),
            grupo.getNombre(),
            grupo.getAsignaturaId(),
            grupo.getDocenteId(),
            grupo.getCuatrimestre(),
            grupo.getCapacidadMaxima()
        );
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public Integer getAsignaturaId() { return asignaturaId; }
    public void setAsignaturaId(Integer asignaturaId) { this.asignaturaId = asignaturaId; }
    
    public Integer getDocenteId() { return docenteId; }
    public void setDocenteId(Integer docenteId) { this.docenteId = docenteId; }
    
    public Integer getCuatrimestre() { return cuatrimestre; }
    public void setCuatrimestre(Integer cuatrimestre) { this.cuatrimestre = cuatrimestre; }
    
    public Integer getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(Integer capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }
}

// Response para lista de grupos
class GrupoListResponse {
    private List<GrupoResponse> grupos;
    private Integer total;

    public GrupoListResponse() {}

    public GrupoListResponse(List<GrupoResponse> grupos, Integer total) {
        this.grupos = grupos;
        this.total = total;
    }

    public static GrupoListResponse fromGrupos(List<Grupo> grupos) {
        List<GrupoResponse> grupoResponses = grupos.stream()
            .map(GrupoResponse::fromGrupo)
            .collect(Collectors.toList());
        return new GrupoListResponse(grupoResponses, grupoResponses.size());
    }

    public List<GrupoResponse> getGrupos() { return grupos; }
    public void setGrupos(List<GrupoResponse> grupos) { this.grupos = grupos; }
    
    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
}

// Response para creación exitosa
class CreateGrupoResponse {
    private String message;
    private GrupoResponse grupo;

    public CreateGrupoResponse() {}

    public CreateGrupoResponse(String message, GrupoResponse grupo) {
        this.message = message;
        this.grupo = grupo;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public GrupoResponse getGrupo() { return grupo; }
    public void setGrupo(GrupoResponse grupo) { this.grupo = grupo; }
}

// Response genérico de mensaje
class MessageResponse {
    private String message;

    public MessageResponse() {}

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

// Response de error
class ErrorResponse {
    private String error;

    public ErrorResponse() {}

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}