package com.gestion.Alumno.domain.entities;

import java.time.LocalDate;

public class Alumno {
    private Integer id;
    private String nombre;
    private String apellido;
    private String matricula;
    private Integer cuatrimestre;
    private String email;
    private Integer programaId;
    private LocalDate fechaIngreso;

    // Constructor vacío
    public Alumno() {
        this.fechaIngreso = LocalDate.now();
    }

    // Constructor completo
    public Alumno(Integer id, String nombre, String apellido, String matricula, 
                  Integer cuatrimestre, String email, Integer programaId, LocalDate fechaIngreso) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.matricula = matricula;
        this.cuatrimestre = cuatrimestre;
        this.email = email;
        this.programaId = programaId;
        this.fechaIngreso = fechaIngreso != null ? fechaIngreso : LocalDate.now();
    }

    // Constructor sin ID (para creación)
    public Alumno(String nombre, String apellido, String matricula, 
                  Integer cuatrimestre, String email, Integer programaId, LocalDate fechaIngreso) {
        this(null, nombre, apellido, matricula, cuatrimestre, email, programaId, fechaIngreso);
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Integer getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(Integer cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getProgramaId() {
        return programaId;
    }

    public void setProgramaId(Integer programaId) {
        this.programaId = programaId;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    // Método de utilidad para obtener nombre completo
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return "Alumno{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", matricula='" + matricula + '\'' +
                ", cuatrimestre=" + cuatrimestre +
                ", email='" + email + '\'' +
                ", programaId=" + programaId +
                ", fechaIngreso=" + fechaIngreso +
                '}';
    }
}