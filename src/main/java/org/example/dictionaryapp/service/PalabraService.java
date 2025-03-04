package org.example.dictionaryapp.service;

import org.example.dictionaryapp.model.Palabra;
import org.example.dictionaryapp.repository.PalabraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PalabraService {

    @Autowired
    private PalabraRepository palabraRepository;

    public List<Palabra> findAll() {
        return palabraRepository.findAll();
    }

    public Optional<Palabra> findById(Long id) {
        return palabraRepository.findById(id);
    }

    public Palabra save(Palabra palabra) {
        return palabraRepository.save(palabra);
    }

    public void deleteById(Long id) {
        palabraRepository.deleteById(id);
    }

    public List<Palabra> findByInicial(String letra) {
        return palabraRepository.findByTerminoStartingWith(letra);
    }

    public List<Palabra> findByCategoria(String categoria) {
        return palabraRepository.findByCategoriaGramatical(categoria);
    }
}

