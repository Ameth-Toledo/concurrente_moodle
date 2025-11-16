package com.gestion.ProgramaEstudio.domain.entities;

import java.time.LocalDateTime;

public class ProgramaEstudio {
    private Integer id;
    private String nombre;
    private Integer numCuatrimestres;
    private LocalDateTime fechaCreacion;

    // Constructor vacío
    public ProgramaEstudio() {
        this.fechaCreacion = LocalDateTime.now();
    }

    // Constructor completo
    public ProgramaEstudio(Integer id, String nombre, Integer numCuatrimestres, LocalDateTime fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.numCuatrimestres = numCuatrimestres;
        this.fechaCreacion = fechaCreacion != null ? fechaCreacion : LocalDateTime.now();
    }

    // Constructor sin ID (para creación)
    public ProgramaEstudio(String nombre, Integer numCuatrimestres, LocalDateTime fechaCreacion) {
        this(null, nombre, numCuatrimestres, fechaCreacion);
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

    public Integer getNumCuatrimestres() {
        return numCuatrimestres;
    }

    public void setNumCuatrimestres(Integer numCuatrimestres) {
        this.numCuatrimestres = numCuatrimestres;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "ProgramaEstudio{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", numCuatrimestres=" + numCuatrimestres +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}