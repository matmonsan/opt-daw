package com.arq_act4.arq_act4;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TablaController {

    @GetMapping(value = "/tabla", produces = "text/html;charset=UTF-8")
    public String tabla(@RequestParam(defaultValue = "1") String filas,
                        @RequestParam(defaultValue = "1") String columnas) {
        int f = ajustar(filas);
        int c = ajustar(columnas);

        String html = "<html><head><meta charset=\"UTF-8\"><title>Tabla</title></head><body>";
        html += "<table border=\"1\"><tr>";
        for (int j = 1; j <= c; j++) {
            html += "<th>Columna " + j + "</th>";
        }
        html += "</tr>";
        for (int i = 1; i <= f; i++) {
            html += "<tr>";
            for (int j = 1; j <= c; j++) {
                html += "<td>Fila " + i + ", Columna " + j + "</td>";
            }
            html += "</tr>";
        }
        html += "</table></body></html>";
        return html;
    }

    // Texto no numérico -> 1; se limita al rango 1-20
    private int ajustar(String texto) {
        int valor = 1;
        try {
            valor = Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            valor = 1;
        }
        if (valor < 1) {
            valor = 1;
        }
        if (valor > 20) {
            valor = 20;
        }
        return valor;
    }
}
