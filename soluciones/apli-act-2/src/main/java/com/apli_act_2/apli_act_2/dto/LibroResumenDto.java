package com.apli_act_2.apli_act_2.dto;

// DTO de resumen para los listados: solo id y título
public class LibroResumenDto {

    private Integer id;
    private String titulo;

    public LibroResumenDto() {
    }

    public LibroResumenDto(Integer id, String titulo) {
        this.id = id;
        this.titulo = titulo;
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
}
