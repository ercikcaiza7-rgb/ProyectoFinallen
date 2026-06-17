package com.example.tienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda.model.DetallePedido;

public interface DetallePedidoRepository
        extends JpaRepository<DetallePedido, Long> {

    List<DetallePedido> findByPedidoId(Long pedidoId);

}
