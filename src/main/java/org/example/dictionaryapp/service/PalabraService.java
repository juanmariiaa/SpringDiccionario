package org.example.dictionaryapp.service;

import org.example.dictionaryapp.exception.RecordNotFoundException;
import org.example.dictionaryapp.model.Palabra;
import org.example.dictionaryapp.model.Definicion;
import org.example.dictionaryapp.repository.PalabraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PalabraService {

    @Autowired
    private PalabraRepository palabraRepository;

    public List<Palabra> getAllPalabras() {
        List<Palabra> palabrasList = palabraRepository.findAll();
        if(palabrasList.size() > 0){
            return palabrasList;
        } else {
            return new ArrayList<Palabra>();
        }
    }

    public Palabra getPalabraById(Long id) throws RecordNotFoundException {
        Optional<Palabra> palabra = palabraRepository.findById(id);
        if(palabra.isPresent()){
            return palabra.get();
        } else {
            throw new RecordNotFoundException("No existe Palabra para el id: ", id);
        }
    }

    public Palabra createPalabra(Palabra palabra) {
        palabra = palabraRepository.save(palabra);
        return palabra;
    }

    public Palabra updatePalabra(Long id, Palabra palabra) throws RecordNotFoundException {
        Optional<Palabra> palabraOptional = palabraRepository.findById(id);
        if (palabraOptional.isPresent()) {
            Palabra newPalabra = palabraOptional.get();
            newPalabra.setTermino(palabra.getTermino());
            newPalabra.setCategoriaGramatical(palabra.getCategoriaGramatical());
            newPalabra = palabraRepository.save(newPalabra);
            return newPalabra;
        } else {
            throw new RecordNotFoundException("No existe Palabra para el id: ", id);
        }
    }

    public void deletePalabra(Long id) throws RecordNotFoundException {
        Optional<Palabra> palabraOptional = palabraRepository.findById(id);
        if (palabraOptional.isPresent()) {
            palabraRepository.delete(palabraOptional.get());
        } else {
            throw new RecordNotFoundException("No existe Palabra para el id: ", id);
        }
    }

    public List<Palabra> getPalabrasByCategoria(String categoria) {
        try {
            return palabraRepository.findByCategoriaGramatical(categoria);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener palabras por categoría: " + e.getMessage());
        }
    }

    public List<Palabra> getPalabrasByInicial(char inicial) {
        try {
            return palabraRepository.findByTerminoStartingWith(inicial);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener palabras por inicial: " + e.getMessage());
        }
    }

    public boolean existePalabra(String termino) {
        try {
            return palabraRepository.existsByTermino(termino);
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia de la palabra: " + e.getMessage());
        }
    }

    public Palabra createPalabraConDefiniciones(Palabra palabra) {
        try {
            for (Definicion definicion : palabra.getDefiniciones()) {
                definicion.setPalabra(palabra);
            }
            return palabraRepository.save(palabra);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear palabra con definiciones: " + e.getMessage());
        }
    }

    public Map<String, Object> obtenerEstadisticas() {
        try {
            List<Palabra> palabras = palabraRepository.findAll();
            int totalPalabras = palabras.size();
            int totalDefiniciones = palabras.stream().mapToInt(p -> p.getDefiniciones().size()).sum();
            Map<String, Long> categorias = palabras.stream()
                    .collect(Collectors.groupingBy(Palabra::getCategoriaGramatical, Collectors.counting()));

            Map<String, Object> estadisticas = new HashMap<>();
            estadisticas.put("totalPalabras", totalPalabras);
            estadisticas.put("totalDefiniciones", totalDefiniciones);
            estadisticas.put("categoriasGramaticales", categorias);

            return estadisticas;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener estadísticas del diccionario: " + e.getMessage());
        }
    }

    public String exportarDiccionario() {
        try {
            List<Palabra> palabras = palabraRepository.findAll();
            StringBuilder builder = new StringBuilder();

            builder.append("Id,Termino,Categoria Gramatical,Definiciones\n");
            for (Palabra palabra : palabras) {
                for (Definicion definicion : palabra.getDefiniciones()) {
                    builder.append(palabra.getId()).append(",");
                    builder.append(palabra.getTermino()).append(",");
                    builder.append(palabra.getCategoriaGramatical()).append(",");
                    builder.append(definicion.getDescripcion()).append("\n");
                }
            }

            return builder.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al exportar el diccionario: " + e.getMessage());
        }
    }

}