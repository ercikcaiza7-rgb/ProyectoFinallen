package com.example.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

        Usuario findByCorreo(String correo);

}
