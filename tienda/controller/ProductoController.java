package com.example.tienda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.tienda.repository.ProductoRepository;

@Controller
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    // ================= INDEX =================
   @GetMapping("/")
public String index(Model model) {

    model.addAttribute(
            "productosInicio",
            productoRepository.findByCategoriaNombre("INICIO")
    );

    return "index";
}

    // ================= ANILLOS =================
    @GetMapping("/anillos")
    public String anillos(Model model) {

        model.addAttribute(
                "productosAnillos",
                productoRepository.findByCategoriaNombre("Anillos")
        );

        return "anillos";
    }

    // ================= ARETES =================
    @GetMapping("/aretes")
    public String aretes(Model model) {

        model.addAttribute(
                "productosAretes",
                productoRepository.findByCategoriaNombre("Aretes")
        );

        return "aretes";
    }

    // ================= COLLARES =================
    @GetMapping("/collares")
    public String collares(Model model) {

        model.addAttribute(
                "productosCollares",
                productoRepository.findByCategoriaNombre("Collares")
        );

        return "collares";
    }

    // ================= BOLSOS =================
    @GetMapping("/bolsos")
    public String bolsos(Model model) {

        model.addAttribute(
                "productosBolsos",
                productoRepository.findByCategoriaNombre("Bolsos")
        );

        return "bolsos";
    }

    // ================= RELOJES =================
    @GetMapping("/relojes")
    public String relojes(Model model) {

        model.addAttribute(
                "productosRelojes",
                productoRepository.findByCategoriaNombre("Relojes")
        );

        return "relojes";
    }

    // ================= LENTES =================
    @GetMapping("/lentes")
    public String lentes(Model model) {

        model.addAttribute(
                "productosLentes",
                productoRepository.findByCategoriaNombre("Lentes")
        );

        return "lentes";
    }

    // ================= PULSERAS =================
    @GetMapping("/pulseras")
    public String pulseras(Model model) {

        model.addAttribute(
                "productosPulseras",
                productoRepository.findByCategoriaNombre("Pulseras")
        );

        return "pulseras";
    }
// ================= NOSOTROS =================
    @GetMapping("/nosotros")
public String nosotros() {
    return "nosotros";
}

}