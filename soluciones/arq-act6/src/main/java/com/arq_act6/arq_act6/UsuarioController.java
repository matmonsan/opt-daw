package com.arq_act6.arq_act6;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UsuarioController {

    // correo -> contraseña
    private Map<String, String> usuarios = new HashMap<>();

    public UsuarioController() {
        usuarios.put("ana@example.com", "246810");
        usuarios.put("luis@example.com", "clave123");
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "redirect:/registro.html";
    }

    @PostMapping("/registro")
    @ResponseBody
    public String registrar(@ModelAttribute Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()
                || usuario.getCorreo() == null || usuario.getCorreo().isBlank()) {
            return "El nombre y el correo son obligatorios";
        }

        boolean generada = false;
        String password = usuario.getPassword();
        if (password == null || password.isBlank()) {
            password = String.format("%06d", new Random().nextInt(1000000));
            generada = true;
        }
        usuarios.put(usuario.getCorreo(), password);

        String respuesta = "Usuario registrado: " + usuario.getNombre() + " " + usuario.getApellidos()
                + "<br>Correo: " + usuario.getCorreo();
        if (generada) {
            respuesta += "<br>Contraseña generada: " + password;
        }
        return respuesta;
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "redirect:/login.html";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestParam String usuario, @RequestParam String password) {
        if (!usuarios.containsKey(usuario)) {
            return "Usuario inexistente";
        }
        if (!usuarios.get(usuario).equals(password)) {
            return "Contraseña incorrecta";
        }
        return "Inicio de sesión correcto";
    }
}
