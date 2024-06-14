package com.aluracursos.literalura.model;


import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String fechaNacimiento;
    private String fechaFallecimiento;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor() {
    }

    public Autor(DatosAutor a ) {
        this.nombre = a.nombre();
        this.fechaNacimiento = a.fechaNacimiento();
        this.fechaFallecimiento = a.fechaFallecimiento();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getFechaFallecimiento() {
        return fechaFallecimiento;
    }

    public void setFechaFallecimiento(String fechaFallecimiento) {
        this.fechaFallecimiento = fechaFallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }


    @Override
    public String toString() {

        var respuesta = """
                
                ✦✦✦✦✦✦✦AUTOR✦✦✦✦✦✦✦
                Nombre: %s
                %s - %s
                
                %s
                ✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦
                """.formatted(nombre, fechaNacimiento, fechaFallecimiento, libros);

        return respuesta;
    }
}
