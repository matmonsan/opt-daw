package com.arq_act5.arq_act5.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arq_act5.arq_act5.models.Pedido;
import com.arq_act5.arq_act5.models.Producto;
import com.arq_act5.arq_act5.models.RespuestaPedido;

@RestController
public class PedidoController {

    @PostMapping("/pedido")
    public RespuestaPedido recibirPedido(@RequestBody Pedido pedido) {
        int totalProductos = 0;
        Double total = 0.0;

        for (Producto p : pedido.getProductos()) {
            totalProductos += p.getCantidad();
            total += p.getPrecioUnitario() * p.getCantidad();
        }

        return new RespuestaPedido(
            pedido.getCliente().getNombre(), 
            totalProductos, 
            total,
            "Pedido recibido correctamente");
    }
}
