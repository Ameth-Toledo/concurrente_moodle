package com.gestion.Docente.domain.dto;

import java.time.LocalDate;

import com.gestion.Docente.domain.entities.Docente;

public class DocenteResponse {
    private Integer id;
    private String nombre;
    private String apellido;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private LocalDate fechaContratacion;

    public DocenteResponse() {}

    public DocenteResponse(Integer id, String nombre, String apellido, String email, 
                          String telefono, LocalDate fechaContratacion) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nombreCompleto = nombre + " " + apellido;
        this.email = email;
        this.telefono = telefono;
        this.fechaContratacion = fechaContratacion;
    }

    public static DocenteResponse fromDocente(Docente docente) {
        return new DocenteResponse(
            docente.getId(),
            docente.getNombre(),
            docente.getApellido(),
            docente.getEmail(),
            docente.getTelefono(),
            docente.getFechaContratacion()
        );
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public LocalDate getFechaContratacion() { return fechaContratacion; }
    public void setFechaContratacion(LocalDate fechaContratacion) { this.fechaContratacion = fechaContratacion; }
}