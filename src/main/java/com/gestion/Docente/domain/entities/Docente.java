package com.gestion.Docente.domain.entities;

import java.time.LocalDate;

public class Docente {
    private Integer id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private LocalDate fechaContratacion;

    // Constructor vacío
    public Docente() {
        this.fechaContratacion = LocalDate.now();
    }

    // Constructor completo
    public Docente(Integer id, String nombre, String apellido, String email, 
                   String telefono, LocalDate fechaContratacion) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.fechaContratacion = fechaContratacion != null ? fechaContratacion : LocalDate.now();
    }

    // Constructor sin ID (para creación)
    public Docente(String nombre, String apellido, String email, 
                   String telefono, LocalDate fechaContratacion) {
        this(null, nombre, apellido, email, telefono, fechaContratacion);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    // Método de utilidad para obtener nombre completo
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return "Docente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", fechaContratacion=" + fechaContratacion +
                '}';
    }
}