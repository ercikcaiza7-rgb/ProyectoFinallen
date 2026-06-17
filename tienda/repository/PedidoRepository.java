package com.example.tienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioId(Long usuarioId);

}
