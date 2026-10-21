package com.arq_reto1.arq_reto1.dtos;

public class ReservaRequest {
    private Integer eventoId;
    private String nombreCliente;
    private String tipoEntrada;
    private Integer cantidad;
    private String codigoCupon;

    public ReservaRequest() {
    }

    public ReservaRequest(Integer eventoId, String nombreCliente, String tipoEntrada, Integer cantidad, String codigoCupon) {
        this.eventoId = eventoId;
        this.nombreCliente = nombreCliente;
        this.tipoEntrada = tipoEntrada;
        this.cantidad = cantidad;
        this.codigoCupon = codigoCupon;
    }

    public Integer getEventoId() {
        return eventoId;
    }

    public void setEventoId(Integer eventoId) {
        this.eventoId = eventoId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTipoEntrada() {
        return tipoEntrada;
    }

    public void setTipoEntrada(String tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getCodigoCupon() {
        return codigoCupon;
    }

    public void setCodigoCupon(String codigoCupon) {
        this.codigoCupon = codigoCupon;
    }
}