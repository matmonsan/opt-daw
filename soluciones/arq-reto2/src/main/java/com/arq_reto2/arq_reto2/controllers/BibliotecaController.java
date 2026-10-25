package com.arq_reto2.arq_reto2.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.arq_reto2.arq_reto2.dtos.LibroResponse;
import com.arq_reto2.arq_reto2.dtos.PrestamoRequest;
import com.arq_reto2.arq_reto2.dtos.PrestamoResponse;
import com.arq_reto2.arq_reto2.models.Libro;

@Controller
public class BibliotecaController {

    private final List<Libro> libros;

    public BibliotecaController() {
        libros = new ArrayList<>();
        libros.add(new Libro(1, "Don Quijote de la Mancha", "Miguel de Cervantes", "Novela", 3));
        libros.add(new Libro(2, "La casa de Bernarda Alba", "Federico García Lorca", "Teatro", 2));
        libros.add(new Libro(3, "Cien años de soledad", "Gabriel García Márquez", "Novela", 4));
        libros.add(new Libro(4, "El principito", "Antoine de Saint-Exupéry", "Fábula", 0));
        libros.add(new Libro(5, "La sombra del viento", "Carlos Ruiz Zafón", "Misterio", 1));
    }

    @GetMapping("/")
    public String mostrarInicio() {
        return "index";
    }

    @GetMapping("/prestamo")
    public String mostrarFormulario() {
        return "prestamo";
    }

    @GetMapping("/libros")
    @ResponseBody
    public List<LibroResponse> buscarLibros(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false, defaultValue = "false") boolean soloDisponibles) {

        List<LibroResponse> resultado = new ArrayList<>();
        for (Libro libro : libros) {
            boolean coincideCategoria = categoria == null || categoria.isBlank()
                    || libro.getCategoria().equalsIgnoreCase(categoria.trim());
            boolean coincideAutor = autor == null || autor.isBlank()
                    || libro.getAutor().toLowerCase().contains(autor.trim().toLowerCase());
            boolean estaDisponible = !soloDisponibles || libro.getEjemplaresDisponibles() > 0;

            if (coincideCategoria && coincideAutor && estaDisponible) {
                resultado.add(new LibroResponse(libro));
            }
        }
        return resultado;
    }

    @GetMapping("/libro")
    @ResponseBody
    public LibroResponse consultarLibro(@RequestParam Integer id) {
        Libro libro = null;
        for (Libro l : libros) {
            if (l.getId().equals(id)) {
                libro = l;
                break;
            }
        }

        if (libro == null) {
            return new LibroResponse("Libro no encontrado");
        }
        return new LibroResponse(libro);
    }

    @PostMapping("/prestar")
    @ResponseBody
    public PrestamoResponse procesarPrestamo(@ModelAttribute PrestamoRequest solicitud) {
        Libro libro = null;
        for (Libro l : libros) {
            if (l.getId().equals(solicitud.getLibroId())) {
                libro = l;
                break;
            }
        }

        if (libro == null) {
            return PrestamoResponse.rechazado("Libro no encontrado");
        }

        if (libro.getEjemplaresDisponibles() <= 0) {
            return PrestamoResponse.rechazado("Sin ejemplares disponibles");
        }

        Integer diasSolicitados = solicitud.getDiasPrestamo();
        if (diasSolicitados == null || diasSolicitados < 1 || diasSolicitados > 30) {
            return PrestamoResponse.rechazado("Duración no válida (1-30 días)");
        }

        int diasTotales = diasSolicitados;
        if (solicitud.isCarnetPremium()) {
            diasTotales += 7;
        }
        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);

        return PrestamoResponse.concedido(
                solicitud.getNombreSocio(),
                libro.getTitulo(),
                diasTotales,
                LocalDate.now().plusDays(diasTotales));
    }
}
