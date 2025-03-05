package org.example.dictionaryapp.service;

import jakarta.persistence.NoResultException;
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
        if (palabrasList.size() > 0) {
            return palabrasList;
        } else {
            return new ArrayList<Palabra>();
        }
    }

    public Palabra getPalabraById(Long id) throws RecordNotFoundException {
        Optional<Palabra> palabra = palabraRepository.findById(id);
        if (palabra.isPresent()) {
            return palabra.get();
        } else {
            throw new RecordNotFoundException("No existe Palabra para el id: ", id);
        }
    }

    public Palabra createPalabra(Palabra palabra) {
        if (palabra == null) {
            throw new IllegalArgumentException("El objeto Palabra no puede ser nulo.");
        }
        if (palabra.getTermino() == null || palabra.getTermino().trim().isEmpty()) {
            throw new IllegalArgumentException("El término de la palabra no puede estar vacío.");
        }
        if (palabra.getCategoriaGramatical() == null || palabra.getCategoriaGramatical().trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría gramatical no puede estar vacía.");
        }
        if (palabraRepository.existsByTermino(palabra.getTermino())) {
            throw new IllegalStateException("La palabra '" + palabra.getTermino() + "' ya existe en el diccionario.");
        }
        try {
            return palabraRepository.save(palabra);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la palabra en la base de datos: " + e.getMessage(), e);
        }
    }

    public Palabra updatePalabra(Long id, Palabra palabra) throws RecordNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la palabra no puede ser nulo.");
        }
        if (palabra == null) {
            throw new IllegalArgumentException("El objeto Palabra no puede ser nulo.");
        }
        if (palabra.getTermino() == null || palabra.getTermino().trim().isEmpty()) {
            throw new IllegalArgumentException("El término de la palabra no puede estar vacío.");
        }
        if (palabra.getCategoriaGramatical() == null || palabra.getCategoriaGramatical().trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría gramatical no puede estar vacía.");
        }
        Optional<Palabra> palabraOptional = palabraRepository.findById(id);
        if (palabraOptional.isPresent()) {
            Palabra palabraExistente = palabraOptional.get();
            if (palabraRepository.existsByTermino(palabra.getTermino())) {
                throw new IllegalStateException("El término '" + palabra.getTermino() + "' ya está en uso por otra palabra.");
            }
            palabraExistente.setTermino(palabra.getTermino());
            palabraExistente.setCategoriaGramatical(palabra.getCategoriaGramatical());
            return palabraRepository.save(palabraExistente);
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

    public List<Palabra> findByCategoriaGramatical(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría gramatical no puede estar vacía.");
        }
        try {
            List<Palabra> palabras = palabraRepository.findByCategoriaGramatical(categoria);
            if (palabras.isEmpty()) {
                throw new NoResultException("No se encontraron palabras para la categoría gramatical: " + categoria);
            }
            return palabras;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar palabras por categoría gramatical: " + e.getMessage(), e);
        }
    }

    public List<Palabra> findByTerminoStartingWith(char inicial) {
        if (!Character.isLetter(inicial)) {
            throw new IllegalArgumentException("El carácter inicial debe ser una letra.");
        }
        return palabraRepository.findByTerminoStartingWith(inicial);
    }

    public boolean existsByTermino(String termino) {
        return palabraRepository.existsByTermino(termino);
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

    public Palabra createPalabraConDefiniciones(Palabra palabra, List<Definicion> definiciones) {
        if (palabra == null) {
            throw new IllegalArgumentException("El objeto Palabra no puede ser nulo.");
        }
        if (palabra.getTermino() == null || palabra.getTermino().trim().isEmpty()) {
            throw new IllegalArgumentException("El término de la palabra no puede estar vacío.");
        }
        if (palabra.getCategoriaGramatical() == null || palabra.getCategoriaGramatical().trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría gramatical no puede estar vacía.");
        }
        if (palabraRepository.existsByTermino(palabra.getTermino())) {
            throw new IllegalStateException("La palabra '" + palabra.getTermino() + "' ya existe en el diccionario.");
        }
        if (definiciones == null || definiciones.isEmpty()) {
            throw new IllegalArgumentException("Debe proporcionar al menos una definición para la palabra.");
        }
        for (Definicion definicion : definiciones) {
            if (definicion.getDescripcion() == null || definicion.getDescripcion().trim().isEmpty()) {
                throw new IllegalArgumentException("La descripción de la definición no puede estar vacía.");
            }
        }
        try {
            palabra.setDefiniciones(definiciones);
            return palabraRepository.save(palabra);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la palabra y sus definiciones en la base de datos: " + e.getMessage(), e);
        }
    }

    public Palabra addDefinicionToPalabra(Long id, Definicion definicion) throws RecordNotFoundException {
        if (definicion == null) {
            throw new IllegalArgumentException("El objeto Definicion no puede ser nulo.");
        }
        if (definicion.getDescripcion() == null || definicion.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la definición no puede estar vacía.");
        }
        Optional<Palabra> palabraOptional = palabraRepository.findById(id);
        if (palabraOptional.isPresent()) {
            Palabra palabraExistente = palabraOptional.get();
            palabraExistente.getDefiniciones().add(definicion);
            return palabraRepository.save(palabraExistente);
        } else {
            throw new RecordNotFoundException("No existe Palabra para el id: ", id);
        }
    }
}