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

    public void deleteDefinicion(Long id) throws RecordNotFoundException {
        Optional<Definicion> definicionOptional = definicionRepository.findById(id);
        if (definicionOptional.isPresent()) {
            definicionRepository.delete(definicionOptional.get());
        } else {
            throw new RecordNotFoundException("No existe Definicion para el id: ", id);
        }
    }
}