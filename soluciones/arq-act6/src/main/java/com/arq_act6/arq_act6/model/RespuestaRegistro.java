package com.arq_act6.arq_act6.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RespuestaRegistro {
    private boolean exito;
    private String mensaje;
    private String nombre;
    private String apellidos;
    private String correo;
    private String passwordGenerada;

    public RespuestaRegistro(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    public RespuestaRegistro(boolean exito, String mensaje, String nombre, String apellidos, String correo,
            String passwordGenerada) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.passwordGenerada = passwordGenerada;
    }

    public boolean isExito() {
        return exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPasswordGenerada() {
        return passwordGenerada;
    }
}
