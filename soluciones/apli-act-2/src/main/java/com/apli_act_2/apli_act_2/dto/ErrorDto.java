package com.apli_act_2.apli_act_2.dto;

// DTO de error: un mensaje para el cliente
public class ErrorDto {

    private String mensaje;

    public ErrorDto() {
    }

    public ErrorDto(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
