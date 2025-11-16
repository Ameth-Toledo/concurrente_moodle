package com.gestion.Asignatura.domain.dto;

import com.gestion.Asignatura.domain.entities.Asignatura;

public class AsignaturaResponse {
    private Integer id;
    private String nombre;
    private Integer cuatrimestre;
    private Integer programaId;
    private Integer creditos;

    public AsignaturaResponse() {}

    public AsignaturaResponse(Integer id, String nombre, Integer cuatrimestre, 
                             Integer programaId, Integer creditos) {
        this.id = id;
        this.nombre = nombre;
        this.cuatrimestre = cuatrimestre;
        this.programaId = programaId;
        this.creditos = creditos;
    }

    public static AsignaturaResponse fromAsignatura(Asignatura asignatura) {
        return new AsignaturaResponse(
            asignatura.getId(),
            asignatura.getNombre(),
            asignatura.getCuatrimestre(),
            asignatura.getProgramaId(),
            asignatura.getCreditos()
        );
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public Integer getCuatrimestre() { return cuatrimestre; }
    public void setCuatrimestre(Integer cuatrimestre) { this.cuatrimestre = cuatrimestre; }
    
    public Integer getProgramaId() { return programaId; }
    public void setProgramaId(Integer programaId) { this.programaId = programaId; }
    
    public Integer getCreditos() { return creditos; }
    public void setCreditos(Integer creditos) { this.creditos = creditos; }
}