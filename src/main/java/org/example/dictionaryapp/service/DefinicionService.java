package org.example.dictionaryapp.service;

import org.example.dictionaryapp.exception.RecordNotFoundException;
import org.example.dictionaryapp.model.Definicion;
import org.example.dictionaryapp.model.Palabra;
import org.example.dictionaryapp.repository.DefinicionRepository;
import org.example.dictionaryapp.repository.PalabraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefinicionService {

    @Autowired
    private DefinicionRepository definicionRepository;

    @Autowired
    private PalabraRepository palabraRepository;

    public List<Definicion> getDefinicionesByPalabraId(Long palabraId) throws RecordNotFoundException {
        Optional<Palabra> palabra = palabraRepository.findById(palabraId);
        if (palabra.isPresent()) {
            return palabra.get().getDefiniciones();
        } else {
            throw new RecordNotFoundException("No existe Palabra para el id: ", palabraId);
        }
    }

    public Definicion createDefinicion(Long palabraId, Definicion definicion) throws RecordNotFoundException {
        Optional<Palabra> palabra = palabraRepository.findById(palabraId);
        if (palabra.isPresent()) {
            definicion.setPalabra(palabra.get());
            return definicionRepository.save(definicion);
        } else {
            throw new RecordNotFoundException("No existe Palabra para el id: ", palabraId);
        }
    }

    public void deleteDefinicion(Long id) throws RecordNotFoundException {
        Optional<Definicion> definicionOptional = definicionRepository.findById(id);
        if (definicionOptional.isPresent()) {
            definicionRepository.delete(definicionOptional.get());
        } else {
            throw new RecordNotFoundException("No existe Definicion para el id: ", id);
        }
    }
}