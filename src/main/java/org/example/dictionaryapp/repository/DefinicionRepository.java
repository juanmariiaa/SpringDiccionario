package org.example.dictionaryapp.repository;


import org.example.dictionaryapp.model.Definicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefinicionRepository extends JpaRepository<Definicion, Long> {
}