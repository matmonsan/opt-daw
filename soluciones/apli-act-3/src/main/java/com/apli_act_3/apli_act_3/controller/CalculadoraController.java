package com.apli_act_3.apli_act_3.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apli_act_3.apli_act_3.service.CalculadoraService;

@RestController
@RequestMapping("/calculadora")
public class CalculadoraController {

    private final CalculadoraService calculadoraService;

    public CalculadoraController(CalculadoraService calculadoraService) {
        this.calculadoraService = calculadoraService;
    }

    // GET /calculadora/suma/8/5 -> 13.0
    @GetMapping("/{operacion}/{numero1}/{numero2}")
    public ResponseEntity<Double> calcular(@PathVariable String operacion,
                                           @PathVariable double numero1,
                                           @PathVariable double numero2) {
        double resultado = calculadoraService.calcular(operacion, numero1, numero2);
        return ResponseEntity.ok(resultado);
    }
}
