package com.arq_act5.arq_act5;

import java.math.BigDecimal;

public class RespuestaPedido {
    private String cliente;
    private int totalProductos;
    private BigDecimal precioTotal;
    private String mensaje;

    public RespuestaPedido(String cliente, int totalProductos, BigDecimal precioTotal, String mensaje) {
        this.cliente = cliente;
        this.totalProductos = totalProductos;
        this.precioTotal = precioTotal;
        this.mensaje = mensaje;
    }

    public String getCliente() {
        return cliente;
    }

    public int getTotalProductos() {
        return totalProductos;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public String getMensaje() {
        return mensaje;
    }
}
