package org.example.dictionaryapp.service;

import org.example.dictionaryapp.model.Definicion;
import org.example.dictionaryapp.model.Palabra;
import org.example.dictionaryapp.repository.DefinicionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefinicionService {
    @Autowired
    private PalabraService palabraService;

    @Autowired
    private DefinicionRepository definicionRepository;

    public List<Definicion> findAll() {
        return definicionRepository.findAll();
    }

    public Optional<Definicion> findById(Long id) {
        return definicionRepository.findById(id);
    }

    public Definicion save(Definicion definicion) {
        return definicionRepository.save(definicion);
    }

    public void deleteById(Long id) {
        definicionRepository.deleteById(id);
    }
    public Definicion addDefinicionToPalabra(Long palabraId, Definicion definicion) {
        Optional<Palabra> palabra = palabraService.findById(palabraId);
        if (palabra.isPresent()) {
            definicion.setPalabra(palabra.get());
            return definicionRepository.save(definicion);
        } else {
            throw new RuntimeException("Palabra not found");
        }
    }
}
