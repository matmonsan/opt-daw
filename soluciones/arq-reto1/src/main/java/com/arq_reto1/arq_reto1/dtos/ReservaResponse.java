package com.arq_reto1.arq_reto1.dtos;

public class ReservaResponse {

    private String estado;
    private String cliente;
    private Integer entradas;
    private Double precioUnitario;
    private Double subtotal;
    private Double descuentoTotal;
    private Double totalAPagar;
    private String motivo;

    public ReservaResponse() {
    }

    // Constructor para respuestas de error/rechazo
    public ReservaResponse(String estado, String motivo) {
        this.estado = estado;
        this.motivo = motivo;
    }

    // Constructor para respuestas de éxito
    public ReservaResponse(String estado, String cliente, Integer entradas, Double precioUnitario, 
                           Double subtotal, Double descuentoTotal, Double totalAPagar) {
        this.estado = estado;
        this.cliente = cliente;
        this.entradas = entradas;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.descuentoTotal = descuentoTotal;
        this.totalAPagar = totalAPagar;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public Integer getEntradas() { return entradas; }
    public void setEntradas(Integer entradas) { this.entradas = entradas; }

    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getDescuentoTotal() { return descuentoTotal; }
    public void setDescuentoTotal(Double descuentoTotal) { this.descuentoTotal = descuentoTotal; }

    public Double getTotalAPagar() { return totalAPagar; }
    public void setTotalAPagar(Double totalAPagar) { this.totalAPagar = totalAPagar; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}