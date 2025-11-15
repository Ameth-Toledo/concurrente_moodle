package com.gestion.Grupo.domain.entities;

public class Grupo {
    private Integer id;
    private String nombre;
    private Integer asignaturaId;
    private Integer docenteId;
    private Integer cuatrimestre;
    private Integer capacidadMaxima;

    // Constructor vacío
    public Grupo() {
        this.capacidadMaxima = 25;
    }

    // Constructor completo
    public Grupo(Integer id, String nombre, Integer asignaturaId, Integer docenteId, 
                 Integer cuatrimestre, Integer capacidadMaxima) {
        this.id = id;
        this.nombre = nombre;
        this.asignaturaId = asignaturaId;
        this.docenteId = docenteId;
        this.cuatrimestre = cuatrimestre;
        this.capacidadMaxima = capacidadMaxima != null ? capacidadMaxima : 25;
    }

    // Constructor sin ID (para creación)
    public Grupo(String nombre, Integer asignaturaId, Integer docenteId, 
                 Integer cuatrimestre, Integer capacidadMaxima) {
        this(null, nombre, asignaturaId, docenteId, cuatrimestre, capacidadMaxima);
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

    public Integer getAsignaturaId() {
        return asignaturaId;
    }

    public void setAsignaturaId(Integer asignaturaId) {
        this.asignaturaId = asignaturaId;
    }

    public Integer getDocenteId() {
        return docenteId;
    }

    public void setDocenteId(Integer docenteId) {
        this.docenteId = docenteId;
    }

    public Integer getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(Integer cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public Integer getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(Integer capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    @Override
    public String toString() {
        return "Grupo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", asignaturaId=" + asignaturaId +
                ", docenteId=" + docenteId +
                ", cuatrimestre=" + cuatrimestre +
                ", capacidadMaxima=" + capacidadMaxima +
                '}';
    }
}