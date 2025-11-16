package com.gestion.Alumno.domain.dto;

import java.time.LocalDate;

import com.gestion.Alumno.domain.entities.Alumno;

public class AlumnoResponse {
    private Integer id;
    private String nombre;
    private String apellido;
    private String nombreCompleto;
    private String matricula;
    private Integer cuatrimestre;
    private String email;
    private Integer programaId;
    private LocalDate fechaIngreso;

    public AlumnoResponse() {}

    public AlumnoResponse(Integer id, String nombre, String apellido, String matricula, 
                         Integer cuatrimestre, String email, Integer programaId, LocalDate fechaIngreso) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nombreCompleto = nombre + " " + apellido;
        this.matricula = matricula;
        this.cuatrimestre = cuatrimestre;
        this.email = email;
        this.programaId = programaId;
        this.fechaIngreso = fechaIngreso;
    }

    public static AlumnoResponse fromAlumno(Alumno alumno) {
        return new AlumnoResponse(
            alumno.getId(),
            alumno.getNombre(),
            alumno.getApellido(),
            alumno.getMatricula(),
            alumno.getCuatrimestre(),
            alumno.getEmail(),
            alumno.getProgramaId(),
            alumno.getFechaIngreso()
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
    
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    
    public Integer getCuatrimestre() { return cuatrimestre; }
    public void setCuatrimestre(Integer cuatrimestre) { this.cuatrimestre = cuatrimestre; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Integer getProgramaId() { return programaId; }
    public void setProgramaId(Integer programaId) { this.programaId = programaId; }
    
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
}