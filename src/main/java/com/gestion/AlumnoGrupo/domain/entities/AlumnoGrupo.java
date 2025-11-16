package com.gestion.AlumnoGrupo.domain.entities;

import java.time.LocalDateTime;

public class AlumnoGrupo {
    private Integer alumnoId;
    private Integer grupoId;
    private LocalDateTime fechaInscripcion;

    // Constructor vacío
    public AlumnoGrupo() {
        this.fechaInscripcion = LocalDateTime.now();
    }

    // Constructor completo
    public AlumnoGrupo(Integer alumnoId, Integer grupoId, LocalDateTime fechaInscripcion) {
        this.alumnoId = alumnoId;
        this.grupoId = grupoId;
        this.fechaInscripcion = fechaInscripcion != null ? fechaInscripcion : LocalDateTime.now();
    }

    // Constructor sin fecha (para creación)
    public AlumnoGrupo(Integer alumnoId, Integer grupoId) {
        this(alumnoId, grupoId, LocalDateTime.now());
    }

    // Getters y Setters
    public Integer getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(Integer alumnoId) {
        this.alumnoId = alumnoId;
    }

    public Integer getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(Integer grupoId) {
        this.grupoId = grupoId;
    }

    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDateTime fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    @Override
    public String toString() {
        return "AlumnoGrupo{" +
                "alumnoId=" + alumnoId +
                ", grupoId=" + grupoId +
                ", fechaInscripcion=" + fechaInscripcion +
                '}';
    }
}