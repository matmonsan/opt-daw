package com.arq_reto2.arq_reto2.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.arq_reto2.arq_reto2.dtos.LibroResponse;
import com.arq_reto2.arq_reto2.dtos.PrestamoRequest;
import com.arq_reto2.arq_reto2.dtos.PrestamoResponse;

class BibliotecaControllerTests {

    @Test
    void buscaLibrosConFiltrosSinDistinguirMayusculas() {
        BibliotecaController controller = new BibliotecaController();

        List<LibroResponse> resultado = controller.buscarLibros("novela", "CERVANTES", true);

        assertEquals(1, resultado.size());
        assertEquals("Don Quijote de la Mancha", resultado.get(0).getTitulo());
    }

    @Test
    void concedePrestamoPremiumYCalculaFechaDevolucion() {
        BibliotecaController controller = new BibliotecaController();
        PrestamoRequest solicitud = solicitud(1, 14, true);

        PrestamoResponse respuesta = controller.procesarPrestamo(solicitud);

        assertEquals("CONCEDIDO", respuesta.getEstado());
        assertEquals("Don Quijote de la Mancha", respuesta.getLibro());
        assertEquals(21, respuesta.getDiasTotales());
        assertEquals(LocalDate.now().plusDays(21), respuesta.getFechaDevolucion());
        assertEquals(2, controller.buscarLibros(null, null, true).get(0).getEjemplaresDisponibles());
    }

    @Test
    void rechazaPrestamosSiguiendoElOrdenDeValidaciones() {
        BibliotecaController controller = new BibliotecaController();

        PrestamoResponse libroInexistente = controller.procesarPrestamo(solicitud(99, 0, false));
        PrestamoResponse sinEjemplares = controller.procesarPrestamo(solicitud(4, 0, false));
        PrestamoResponse duracionInvalida = controller.procesarPrestamo(solicitud(1, 31, false));

        assertEquals("Libro no encontrado", libroInexistente.getMotivo());
        assertEquals("Sin ejemplares disponibles", sinEjemplares.getMotivo());
        assertEquals("Duración no válida (1-30 días)", duracionInvalida.getMotivo());
        assertEquals(3, controller.buscarLibros(null, null, true).get(0).getEjemplaresDisponibles());
    }

    @Test
    void consultaLibroDevuelveErrorCuandoNoExiste() {
        BibliotecaController controller = new BibliotecaController();

        LibroResponse respuesta = controller.consultarLibro(99);

        assertEquals("Libro no encontrado", respuesta.getMensaje());
    }

    private PrestamoRequest solicitud(Integer libroId, Integer diasPrestamo, boolean carnetPremium) {
        PrestamoRequest solicitud = new PrestamoRequest();
        solicitud.setLibroId(libroId);
        solicitud.setNombreSocio("Marta Ruiz");
        solicitud.setDiasPrestamo(diasPrestamo);
        solicitud.setCarnetPremium(carnetPremium);
        return solicitud;
    }
}
