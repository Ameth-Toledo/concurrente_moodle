package com.gestion.AlumnoGrupo.domain.dto;

import java.time.LocalDateTime;

import com.gestion.AlumnoGrupo.domain.entities.AlumnoGrupo;

public class AlumnoGrupoResponse {
    private Integer alumnoId;
    private Integer grupoId;
    private LocalDateTime fechaInscripcion;

    public AlumnoGrupoResponse() {}

    public AlumnoGrupoResponse(Integer alumnoId, Integer grupoId, LocalDateTime fechaInscripcion) {
        this.alumnoId = alumnoId;
        this.grupoId = grupoId;
        this.fechaInscripcion = fechaInscripcion;
    }

    public static AlumnoGrupoResponse fromAlumnoGrupo(AlumnoGrupo alumnoGrupo) {
        return new AlumnoGrupoResponse(
            alumnoGrupo.getAlumnoId(),
            alumnoGrupo.getGrupoId(),
            alumnoGrupo.getFechaInscripcion()
        );
    }

    // Getters y Setters
    public Integer getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Integer alumnoId) { this.alumnoId = alumnoId; }
    
    public Integer getGrupoId() { return grupoId; }
    public void setGrupoId(Integer grupoId) { this.grupoId = grupoId; }
    
    public LocalDateTime getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(LocalDateTime fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }
}