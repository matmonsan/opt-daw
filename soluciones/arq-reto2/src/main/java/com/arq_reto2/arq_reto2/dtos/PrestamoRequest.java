package com.arq_reto2.arq_reto2.dtos;

public class PrestamoRequest {
    private Integer libroId;
    private String nombreSocio;
    private Integer diasPrestamo;
    private Boolean carnetPremium;

    public PrestamoRequest() {
    }

    public Integer getLibroId() {
        return libroId;
    }

    public void setLibroId(Integer libroId) {
        this.libroId = libroId;
    }

    public String getNombreSocio() {
        return nombreSocio;
    }

    public void setNombreSocio(String nombreSocio) {
        this.nombreSocio = nombreSocio;
    }

    public Integer getDiasPrestamo() {
        return diasPrestamo;
    }

    public void setDiasPrestamo(Integer diasPrestamo) {
        this.diasPrestamo = diasPrestamo;
    }

    public boolean isCarnetPremium() {
        return carnetPremium;
    }

    public void setCarnetPremium(boolean carnetPremium) {
        this.carnetPremium = carnetPremium;
    }
}
