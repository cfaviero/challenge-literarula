package com.aluracursos.literalura.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.util.List;
@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;
    private String idiomas;
    private Integer numeroDeDescargas;


    public Libro() {
    }


    public Libro(DatosLibro libroRecibido, Autor autor) {
        this.titulo = libroRecibido.titulo();
        this.autor = autor;
        this.idiomas = libroRecibido.idiomas().get(0);
        this.numeroDeDescargas = libroRecibido.numeroDeDescargas();
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public Integer getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Integer numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }


    @Override
    public String toString() {
        var respuesta = """
                
                ✦✦✦✦✦✦✦LIBRO✦✦✦✦✦✦✦
                Titulo: %s
                Autor: %s
                Idioma: %s
                Numero de Descargas: %d
                ✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦
                """.formatted(titulo, autor.getNombre(), idiomas, numeroDeDescargas);
        return respuesta;
    }
}