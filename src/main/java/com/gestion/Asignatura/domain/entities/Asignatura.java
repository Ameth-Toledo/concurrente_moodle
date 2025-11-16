package com.gestion.Asignatura.domain.entities;

public class Asignatura {
    private Integer id;
    private String nombre;
    private Integer cuatrimestre;
    private Integer programaId;
    private Integer creditos;

    // Constructor vacío
    public Asignatura() {
        this.creditos = 6; // Valor por defecto
    }

    // Constructor completo
    public Asignatura(Integer id, String nombre, Integer cuatrimestre, 
                      Integer programaId, Integer creditos) {
        this.id = id;
        this.nombre = nombre;
        this.cuatrimestre = cuatrimestre;
        this.programaId = programaId;
        this.creditos = creditos != null ? creditos : 6;
    }

    // Constructor sin ID (para creación)
    public Asignatura(String nombre, Integer cuatrimestre, 
                      Integer programaId, Integer creditos) {
        this(null, nombre, cuatrimestre, programaId, creditos);
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(Integer cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public Integer getProgramaId() {
        return programaId;
    }

    public void setProgramaId(Integer programaId) {
        this.programaId = programaId;
    }

    public Integer getCreditos() {
        return creditos;
    }

    public void setCreditos(Integer creditos) {
        this.creditos = creditos;
    }

    @Override
    public String toString() {
        return "Asignatura{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cuatrimestre=" + cuatrimestre +
                ", programaId=" + programaId +
                ", creditos=" + creditos +
                '}';
    }
}