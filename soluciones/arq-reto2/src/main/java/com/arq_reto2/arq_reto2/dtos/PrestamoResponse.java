package com.arq_reto2.arq_reto2.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrestamoResponse {
    private String estado;
    private String socio;
    private String libro;
    private Integer diasTotales;
    private LocalDate fechaDevolucion;
    private String motivo;

    public PrestamoResponse() {
    }

    private PrestamoResponse(String estado, String socio, String libro, Integer diasTotales,
            LocalDate fechaDevolucion, String motivo) {
        this.estado = estado;
        this.socio = socio;
        this.libro = libro;
        this.diasTotales = diasTotales;
        this.fechaDevolucion = fechaDevolucion;
        this.motivo = motivo;
    }

    public static PrestamoResponse concedido(String socio, String libro, Integer diasTotales,
            LocalDate fechaDevolucion) {
        return new PrestamoResponse("CONCEDIDO", socio, libro, diasTotales, fechaDevolucion, null);
    }

    public static PrestamoResponse rechazado(String motivo) {
        return new PrestamoResponse("RECHAZADO", null, null, null, null, motivo);
    }

    public String getEstado() {
        return estado;
    }

    public String getSocio() {
        return socio;
    }

    public String getLibro() {
        return libro;
    }

    public Integer getDiasTotales() {
        return diasTotales;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public String getMotivo() {
        return motivo;
    }
}
