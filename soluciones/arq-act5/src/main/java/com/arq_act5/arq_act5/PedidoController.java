package com.arq_act5.arq_act5;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PedidoController {

    @PostMapping("/pedido")
    public RespuestaPedido recibirPedido(@RequestBody Pedido pedido) {
        int totalProductos = 0;
        BigDecimal total = BigDecimal.ZERO;

        for (Producto p : pedido.getProductos()) {
            totalProductos += p.getCantidad();
            total = total.add(p.getPrecioUnitario().multiply(BigDecimal.valueOf(p.getCantidad())));
        }

        return new RespuestaPedido(pedido.getCliente().getNombre(), totalProductos, total,
                "Pedido recibido correctamente");
    }
}
