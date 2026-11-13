package com.apli_act_3.apli_act_3.service;

import org.springframework.stereotype.Service;

import com.apli_act_3.apli_act_3.exception.DivisionPorCeroException;
import com.apli_act_3.apli_act_3.exception.NumeroFueraDeRangoException;
import com.apli_act_3.apli_act_3.exception.NumeroNegativoException;
import com.apli_act_3.apli_act_3.exception.OperacionNoValidaException;

@Service
public class CalculadoraService {

    private static final double MINIMO = 0;
    private static final double MAXIMO = 100;

    public double calcular(String operacion, double numero1, double numero2) {
        validar(numero1);
        validar(numero2);

        switch (operacion) {
            case "suma":
                return numero1 + numero2;
            case "resta":
                return numero1 - numero2;
            case "multiplicacion":
                return numero1 * numero2;
            case "division":
                if (numero2 == 0) {
                    throw new DivisionPorCeroException("No se puede dividir entre cero");
                }
                return numero1 / numero2;
            default:
                throw new OperacionNoValidaException("Operación no válida: " + operacion
                        + ". Usa suma, resta, multiplicacion o division");
        }
    }

    private void validar(double numero) {
        if (numero < MINIMO) {
            throw new NumeroNegativoException("Los números no pueden ser negativos: " + numero);
        }
        if (numero > MAXIMO) {
            throw new NumeroFueraDeRangoException("Los números deben estar entre 0 y 100: " + numero);
        }
    }
}
