package com.apli_act_2.apli_act_2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apli_act_2.apli_act_2.dto.LibroCrearDto;
import com.apli_act_2.apli_act_2.dto.LibroResumenDto;
import com.apli_act_2.apli_act_2.service.LibroService;

@RestController
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody LibroCrearDto entrada) {
        return libroService.crear(entrada);
    }

    @GetMapping
    public ResponseEntity<List<LibroResumenDto>> listar() {
        return libroService.listar();
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> obtener(@RequestParam Integer id) {
        return libroService.obtener(id);
    }

    @PostMapping("/prestamo")
    public ResponseEntity<?> prestar(@RequestParam Integer id) {
        return libroService.prestar(id);
    }

    @PostMapping("/devolucion")
    public ResponseEntity<?> devolver(@RequestParam Integer id) {
        return libroService.devolver(id);
    }
}
