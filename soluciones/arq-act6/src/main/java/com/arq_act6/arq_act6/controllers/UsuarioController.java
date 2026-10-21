package com.arq_act6.arq_act6.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.arq_act6.arq_act6.model.Login;
import com.arq_act6.arq_act6.model.RespuestaLogin;
import com.arq_act6.arq_act6.model.RespuestaRegistro;
import com.arq_act6.arq_act6.model.Usuario;

@Controller
public class UsuarioController {

    // Mapa para correo -> contraseña
    private Map<String, String> usuarios = new HashMap<>();

    //Inicialización en constructor
    public UsuarioController() {
        usuarios.put("ana@example.com", "246810");
        usuarios.put("luis@example.com", "clave123");
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    @ResponseBody
    public RespuestaRegistro registrar(@ModelAttribute Usuario usuario) {

        if (usuario.getNombre() == null || usuario.getNombre().isBlank() || usuario.getCorreo() == null || usuario.getCorreo().isBlank()) {
            return new RespuestaRegistro(false, "El nombre y el correo son obligatorios");
        }

        String passwordGenerada = null;
        String password = usuario.getPassword();
        if (password == null || password.isBlank()) {
            password = String.format("%06d", new Random().nextInt(1000000));
            passwordGenerada = password;
        }
        usuarios.put(usuario.getCorreo(), password);

        return new RespuestaRegistro(true, "Usuario registrado", usuario.getNombre(), usuario.getApellidos(),
                usuario.getCorreo(), passwordGenerada);
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public RespuestaLogin login(@ModelAttribute Login login) {
        if (!usuarios.containsKey(login.getUsuario())) {
            return new RespuestaLogin(false, "Usuario inexistente");
        }
        if (!usuarios.get(login.getUsuario()).equals(login.getPassword())) {
            return new RespuestaLogin(false, "Contraseña incorrecta");
        }
        return new RespuestaLogin(true, "Inicio de sesión correcto");
    }
}
