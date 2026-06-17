package com.example.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda.model.Contacto;

public interface ContactoRepository
        extends JpaRepository<Contacto, Long> {

}
