package com.arq_act6.arq_act6.model;

public class RespuestaLogin {
    private boolean exito;
    private String mensaje;

    public RespuestaLogin(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    public boolean isExito() {
        return exito;
    }

    public String getMensaje() {
        return mensaje;
    }
}
