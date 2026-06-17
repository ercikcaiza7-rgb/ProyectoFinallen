package com.example.tienda.controller;

// Permite la inyección automática de dependencias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.tienda.model.Contacto;
import com.example.tienda.repository.ContactoRepository;

// Define esta clase como un controlador web
@Controller
public class ContactoController {

    // Inyección automática del repositorio de contacto
    @Autowired
    private ContactoRepository contactoRepository;

    // Muestra el formulario de contacto
    @GetMapping("/contacto")
    public String mostrarFormulario(Model model) {

        // Envía un objeto Contacto vacío a la vista
        // para enlazar los campos del formulario
        model.addAttribute(
                "contacto",
                new Contacto()
        );

        // Retorna la vista contacto.html
        return "contacto";
    }

    // Procesa el envío del formulario de contacto
    @PostMapping("/contacto")
    public String guardarContacto(
            Contacto contacto
    ) {

        // Guarda los datos ingresados por el usuario
        // en la base de datos
        contactoRepository.save(contacto);

        // Redirecciona nuevamente al formulario
        // mostrando un parámetro que indica envío exitoso
        return "redirect:/contacto?enviado";
    }

}