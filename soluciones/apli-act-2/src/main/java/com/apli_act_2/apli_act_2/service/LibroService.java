package com.apli_act_2.apli_act_2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.apli_act_2.apli_act_2.dto.ErrorDto;
import com.apli_act_2.apli_act_2.dto.LibroCrearDto;
import com.apli_act_2.apli_act_2.dto.LibroDto;
import com.apli_act_2.apli_act_2.dto.LibroResumenDto;
import com.apli_act_2.apli_act_2.model.Libro;

@Service
public class LibroService {

    // Datos en memoria
    private final List<Libro> libros = new ArrayList<>();

    // Último id asignado. Empieza en 0 para que el primer libro tenga el id 1
    private static Integer idActual = 0;

    // 201 Created con el libro, 400 si los datos no son válidos, 409 si el ISBN ya existe
    public ResponseEntity<?> crear(LibroCrearDto entrada) {
        if (entrada.getTitulo() == null || entrada.getTitulo().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDto("El título no puede estar vacío"));
        }
        if (entrada.getAutor() == null || entrada.getAutor().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDto("El autor no puede estar vacío"));
        }
        if (entrada.getIsbn() == null || entrada.getIsbn().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDto("El ISBN no puede estar vacío"));
        }
        if (entrada.getAnioPublicacion() == null || entrada.getAnioPublicacion() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDto("El año de publicación debe ser mayor que 0"));
        }

        // No puede haber dos libros con el mismo ISBN
        for (Libro libro : libros) {
            if (libro.getIsbn().equals(entrada.getIsbn())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorDto("Ya existe un libro con el ISBN " + entrada.getIsbn()));
            }
        }

        idActual = idActual + 1;

        // Un libro nuevo nunca está prestado y empieza sin notas internas
        Libro libro = new Libro(idActual, entrada.getTitulo(), entrada.getAutor(),
                entrada.getIsbn(), entrada.getAnioPublicacion(), false, "");
        libros.add(libro);

        LibroDto creado = new LibroDto(libro.getId(), libro.getTitulo(), libro.getAutor(),
                libro.getIsbn(), libro.getAnioPublicacion(), libro.getPrestado());
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // 200 OK con la lista reducida
    public ResponseEntity<List<LibroResumenDto>> listar() {
        List<LibroResumenDto> resumen = new ArrayList<>();
        for (Libro libro : libros) {
            resumen.add(new LibroResumenDto(libro.getId(), libro.getTitulo()));
        }
        return ResponseEntity.ok(resumen);
    }

    // 200 OK con el libro, o 404 si no existe
    public ResponseEntity<?> obtener(Integer id) {
        for (Libro libro : libros) {
            if (libro.getId().equals(id)) {
                LibroDto dto = new LibroDto(libro.getId(), libro.getTitulo(), libro.getAutor(),
                        libro.getIsbn(), libro.getAnioPublicacion(), libro.getPrestado());
                return ResponseEntity.ok(dto);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto("No existe ningún libro con id " + id));
    }

    // 200 OK con el libro prestado, 404 si no existe, 409 si ya estaba prestado
    public ResponseEntity<?> prestar(Integer id) {
        for (Libro libro : libros) {
            if (libro.getId().equals(id)) {
                if (libro.getPrestado()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(new ErrorDto("El libro ya está prestado"));
                }
                libro.setPrestado(true);
                LibroDto dto = new LibroDto(libro.getId(), libro.getTitulo(), libro.getAutor(),
                        libro.getIsbn(), libro.getAnioPublicacion(), libro.getPrestado());
                return ResponseEntity.ok(dto);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto("No existe ningún libro con id " + id));
    }

    // 200 OK con el libro devuelto, 404 si no existe, 409 si no estaba prestado
    public ResponseEntity<?> devolver(Integer id) {
        for (Libro libro : libros) {
            if (libro.getId().equals(id)) {
                if (!libro.getPrestado()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(new ErrorDto("El libro no está prestado"));
                }
                libro.setPrestado(false);
                LibroDto dto = new LibroDto(libro.getId(), libro.getTitulo(), libro.getAutor(),
                        libro.getIsbn(), libro.getAnioPublicacion(), libro.getPrestado());
                return ResponseEntity.ok(dto);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto("No existe ningún libro con id " + id));
    }
}
