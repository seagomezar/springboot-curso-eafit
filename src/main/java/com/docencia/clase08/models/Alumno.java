package com.docencia.clase08.models;
import jakarta.persistence.*;

@Entity
@Table(name = "alumnos")
public class Alumno {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    
    private Integer edad;
    
    // Constructors
    public Alumno() {
    }
    
    public Alumno(String nombre, Integer edad) {
        this.nombre = nombre;
        this.edad = edad;
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
    public Integer getEdad() {
        return edad;
    }
    public void setEdad(Integer edad) {
        this.edad = edad;
    }
}
