package org.example.dictionaryapp.repository;

import org.example.dictionaryapp.model.Palabra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PalabraRepository extends JpaRepository<Palabra, Long> {

    @Query("SELECT p FROM Palabra p WHERE p.categoriaGramatical = :categoria")
    List<Palabra> findByCategoriaGramatical(@Param("categoria") String categoria);

    @Query("SELECT p FROM Palabra p WHERE p.termino LIKE :inicial%")
    List<Palabra> findByTerminoStartingWith(@Param("inicial") char inicial);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Palabra p WHERE p.termino = :termino")
    boolean existsByTermino(@Param("termino") String termino);
}