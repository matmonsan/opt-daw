package com.apli_act_1.apli_act_1.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apli_act_1.apli_act_1.model.Producto;
import com.apli_act_1.apli_act_1.model.Respuesta;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private static final Integer MAXIMO_PRODUCTOS = 5;

    // Último id asignado. Empieza en 3 porque la lista arranca con los productos 1, 2 y 3
    private static Integer idActual = 3;

    // Datos en memoria: la lista empieza con tres productos
    private final List<Producto> productos = new ArrayList<>();

    public ProductoController() {
        productos.add(new Producto(1, "Producto 1", 10.0));
        productos.add(new Producto(2, "Producto 2", 15.5));
        productos.add(new Producto(3, "Producto 3", 20.0));
    }

    // Alternativa: llenar la lista en la misma declaración con List.of (sin constructor).
    // List.of crea una lista inmutable, por eso se envuelve en new ArrayList<>(...)
    // para poder añadir productos después con el POST.
    //
    // private final List<Producto> productos = new ArrayList<>(List.of(
    //         new Producto(1, "Producto 1", 10.0),
    //         new Producto(2, "Producto 2", 15.5),
    //         new Producto(3, "Producto 3", 20.0)
    // ));

    // 200 OK con la lista, o 404 Not Found con una Respuesta si está vacía
    @GetMapping
    public ResponseEntity<?> listar() {
        if (productos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Respuesta("No hay productos"));
        }
        return ResponseEntity.ok(productos);
    }

    // 200 OK con el producto, o 404 Not Found con una Respuesta si no existe
    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(@RequestParam Integer id) {
        Producto producto = buscarPorId(id).orElse(null);

        if (producto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Respuesta("No existe ningún producto con id " + id));
        }
        return ResponseEntity.ok(producto);
    }

    // 200 OK con una Respuesta, o 204 No Content sin cuerpo si no hay productos
    @GetMapping("/resumen")
    public ResponseEntity<Respuesta> resumen() {
        if (productos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(new Respuesta("Hay " + productos.size() + " productos"));
    }

    // 201 Created con el producto nuevo, o 409 Conflict si ya hay 5 productos
    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@RequestBody Producto producto) {
        if (productos.size() >= MAXIMO_PRODUCTOS) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Respuesta("El catálogo está lleno"));
        }

        // El nombre y el precio llegan en el JSON; el id lo asigna el servidor
        idActual = idActual + 1;
        producto.setId(idActual);

        productos.add(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    // Recorre la lista con un bucle y devuelve el producto si lo encuentra
    private Optional<Producto> buscarPorId(Integer id) {
        for (Producto producto : productos) {
            if (producto.getId().equals(id)) {
                return Optional.of(producto);
            }
        }
        return Optional.empty();
    }
}
