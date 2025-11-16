package com.gestion.ProgramaEstudio.domain.dto;

import java.time.LocalDateTime;

import com.gestion.ProgramaEstudio.domain.entities.ProgramaEstudio;

public class ProgramaEstudioResponse {
    private Integer id;
    private String nombre;
    private Integer numCuatrimestres;
    private LocalDateTime fechaCreacion;

    public ProgramaEstudioResponse() {}

    public ProgramaEstudioResponse(Integer id, String nombre, Integer numCuatrimestres, LocalDateTime fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.numCuatrimestres = numCuatrimestres;
        this.fechaCreacion = fechaCreacion;
    }

    public static ProgramaEstudioResponse fromProgramaEstudio(ProgramaEstudio programaEstudio) {
        return new ProgramaEstudioResponse(
            programaEstudio.getId(),
            programaEstudio.getNombre(),
            programaEstudio.getNumCuatrimestres(),
            programaEstudio.getFechaCreacion()
        );
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public Integer getNumCuatrimestres() { return numCuatrimestres; }
    public void setNumCuatrimestres(Integer numCuatrimestres) { this.numCuatrimestres = numCuatrimestres; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}

// Response con estadísticas
class ProgramaEstudioStatsResponse extends ProgramaEstudioResponse {
    private Integer totalAlumnos;
    private Integer totalAsignaturas;

    public ProgramaEstudioStatsResponse() {}

    public ProgramaEstudioStatsResponse(Integer id, String nombre, Integer numCuatrimestres, 
                                       LocalDateTime fechaCreacion, Integer totalAlumnos, Integer totalAsignaturas) {
        super(id, nombre, numCuatrimestres, fechaCreacion);
        this.totalAlumnos = totalAlumnos;
        this.totalAsignaturas = totalAsignaturas;
    }

    public Integer getTotalAlumnos() { return totalAlumnos; }
    public void setTotalAlumnos(Integer totalAlumnos) { this.totalAlumnos = totalAlumnos; }
    
    public Integer getTotalAsignaturas() { return totalAsignaturas; }
    public void setTotalAsignaturas(Integer totalAsignaturas) { this.totalAsignaturas = totalAsignaturas; }
}