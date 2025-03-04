package org.example.dictionaryapp.repository;


import org.example.dictionaryapp.model.Definicion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefinicionRepository extends JpaRepository<Definicion, Long> {
}