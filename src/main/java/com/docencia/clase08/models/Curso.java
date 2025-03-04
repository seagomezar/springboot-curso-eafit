package com.docencia.clase08.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cursos")
public class Curso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    
    private Integer duracion;

    private String descripcion;
    
    // Constructors
    public Curso() {
    }
    
    public Curso(String nombre, Integer duracion) {
        this.nombre = nombre;
        this.duracion = duracion;
    }
    // Getters and Setters
    public Long getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Integer getDuracion() {
        return duracion;
    }
    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setdescripcion(String nombre) {
        this.nombre = descripcion;
    }
}