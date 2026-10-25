package com.arq_reto2.arq_reto2.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.arq_reto2.arq_reto2.models.Libro;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LibroResponse {
    private Integer id;
    private String titulo;
    private String autor;
    private String categoria;
    private Integer ejemplaresDisponibles;
    private String mensaje;

    public LibroResponse() {
    }

    public LibroResponse(Libro libro) {
        this.id = libro.getId();
        this.titulo = libro.getTitulo();
        this.autor = libro.getAutor();
        this.categoria = libro.getCategoria();
        this.ejemplaresDisponibles = libro.getEjemplaresDisponibles();
    }

    public LibroResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    public Integer getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public Integer getEjemplaresDisponibles() {
        return ejemplaresDisponibles;
    }

    public String getMensaje() {
        return mensaje;
    }
}
