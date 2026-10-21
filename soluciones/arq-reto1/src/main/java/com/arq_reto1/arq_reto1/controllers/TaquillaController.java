package com.arq_reto1.arq_reto1.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.arq_reto1.arq_reto1.dtos.ReservaRequest;
import com.arq_reto1.arq_reto1.dtos.ReservaResponse;
import com.arq_reto1.arq_reto1.models.Obra;

@Controller
public class TaquillaController {

    // Cartelera inicial almacenada en memoria
    private final List<Obra> cartelera = new CopyOnWriteArrayList<>(Arrays.asList(
            new Obra(101, "La Casa de Bernarda Alba", 25.0, 10),
            new Obra(102, "Yerma", 32.0, 5),
            new Obra(103, "El Tragaluz", 18.0, 20)
    ));

    // Endpoint GET: muestra el formulario HTML
    @GetMapping("/reserva")
    public String mostrarFormulario() {
        return "reserva";
    }

    // Endpoint GET: consulta la cartelera
    @GetMapping("/cartelera")
    @ResponseBody
    public List<Obra> consultarCartelera(
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false, defaultValue = "false") Boolean soloDisponibles) {

        List<Obra> resultado = new ArrayList<>();

        for (Obra o : cartelera) {
            boolean cumplePrecio = (precioMax == null || o.getPrecioBase() <= precioMax);
            boolean cumpleDisponibilidad = (!soloDisponibles || o.getAsientosLibres() > 0);

            if (cumplePrecio && cumpleDisponibilidad) {
                resultado.add(o);
            }
        }

        return resultado;
    }

    // Endpoint POST: gestiona una compra
    @PostMapping("/comprar")
    @ResponseBody
    public ReservaResponse procesarCompra(@ModelAttribute ReservaRequest solicitud) {

        //Se comprueba que exista la obra a reservar
        Obra obra = null;
        for (Obra o : cartelera) {
            if (o.getId().equals(solicitud.getEventoId())) {
                obra = o;
                break;
            }
        }

        if (obra == null) {
            return new ReservaResponse("RECHAZADA", "Obra no encontrada con ID: " + solicitud.getEventoId());
        }

        //Verificación de aforo
        if (solicitud.getCantidad() > obra.getAsientosLibres()) {
            return new ReservaResponse("RECHAZADA", "Aforo insuficiente. Asientos disponibles restantes: " + obra.getAsientosLibres());
        }

        //Modificación de asientos
        obra.setAsientosLibres(obra.getAsientosLibres() - solicitud.getCantidad());

        //Precio base según la zona
        double precioUnitario = obra.getPrecioBase();
        if ("VIP".equalsIgnoreCase(solicitud.getTipoEntrada())) {
            precioUnitario *= 1.20;
        }

        double subtotal = precioUnitario * solicitud.getCantidad();
        double importeConDescuento = subtotal;

        //Aplicación de descuentos
        if (solicitud.getCantidad() >= 4) {
            importeConDescuento *= 0.90;
        }

        if ("DESCUENTO15".equalsIgnoreCase(solicitud.getCodigoCupon())) {
            importeConDescuento *= 0.85;
        }

        double totalAPagar = redondear(importeConDescuento);
        double descuentoTotal = redondear(subtotal - totalAPagar);

        // Reserva realizada, se devuelve en JSON
        return new ReservaResponse(
                "CONFIRMADA",
                solicitud.getNombreCliente(),
                solicitud.getCantidad(),
                redondear(precioUnitario),
                redondear(subtotal),
                descuentoTotal,
                totalAPagar
        );
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
