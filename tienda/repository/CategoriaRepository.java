package com.example.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}