package com.example.tienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda.model.Carrito;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    Carrito findByUsuario_IdAndProducto_Id(
            Long usuarioId,
            Long productoId
    );

    List<Carrito> findByUsuarioId(Long usuarioId);

}
