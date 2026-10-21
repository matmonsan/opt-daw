package com.arq_reto1.arq_reto1.models;

public class Obra {
    private Integer id;
    private String titulo;
    private Double precioBase;
    private Integer asientosLibres;

    public Obra() {
    }

    public Obra(Integer id, String titulo, Double precioBase, Integer asientosLibres) {
        this.id = id;
        this.titulo = titulo;
        this.precioBase = precioBase;
        this.asientosLibres = asientosLibres;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(Double precioBase) {
        this.precioBase = precioBase;
    }

    public Integer getAsientosLibres() {
        return asientosLibres;
    }

    public void setAsientosLibres(Integer asientosLibres) {
        this.asientosLibres = asientosLibres;
    }
}
