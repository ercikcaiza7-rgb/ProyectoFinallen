package com.example.tienda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.tienda.model.Usuario;
import com.example.tienda.repository.UsuarioRepository;
import com.example.tienda.service.PasswordService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordService passwordService;

    // ================= REGISTRO =================

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {

        model.addAttribute("usuario", new Usuario());

        return "registro";
    }

@PostMapping("/registro")
public String registrarUsuario(
@ModelAttribute Usuario usuario,
Model model
) {


Usuario existente =
        usuarioRepository.findByCorreo(
                usuario.getCorreo()
        );

if (existente != null) {

    model.addAttribute(
            "errorCorreo",
            "Este correo ya está registrado"
    );

    return "registro";
}

usuario.setPassword(
        passwordService.encriptar(
                usuario.getPassword()
        )
);

usuarioRepository.save(usuario);

return "redirect:/login";


}


    // ================= LOGIN =================

    @GetMapping("/login")
    public String mostrarLogin() {

        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String correo,
            @RequestParam String password,
            Model model,
            HttpSession session
    ) {

        Usuario usuario =
                usuarioRepository.findByCorreo(correo);

        

if (usuario != null &&
passwordService.verificar(
password,
usuario.getPassword()
)) {

session.setAttribute("usuario", usuario);

return "redirect:/";

}

        model.addAttribute(
                "error",
                "Correo o contraseña incorrectos"
        );

        return "login";
    }

    // ================= LOGOUT =================

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }
}
