package com.example.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda.model.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long> {

}
