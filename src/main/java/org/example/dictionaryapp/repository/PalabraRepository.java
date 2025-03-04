package org.example.dictionaryapp.repository;

import org.example.dictionaryapp.model.Palabra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PalabraRepository extends JpaRepository<Palabra, Long> {
    List<Palabra> findByTerminoStartingWith(String letra);
    List<Palabra> findByCategoriaGramatical(String categoriaGramatical);
}