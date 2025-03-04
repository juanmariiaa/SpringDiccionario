package org.example.dictionaryapp.controller;

import org.example.dictionaryapp.model.Definicion;
import org.example.dictionaryapp.model.Palabra;
import org.example.dictionaryapp.service.DefinicionService;
import org.example.dictionaryapp.service.PalabraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/palabras")
public class PalabraController {

    @Autowired
    private PalabraService palabraService;

    @Autowired
    private DefinicionService definicionService;

    // Obtener todas las palabras del diccionario
    @GetMapping
    public List<Palabra> getAllPalabras() {
        return palabraService.findAll();
    }

    // Obtener una palabra específica con sus definiciones
    @GetMapping("/{id}")
    public ResponseEntity<Palabra> getPalabraById(@PathVariable Long id) {
        Optional<Palabra> palabra = palabraService.findById(id);
        return palabra.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva palabra sin definiciones
    @PostMapping
    public Palabra createPalabra(@RequestBody Palabra palabra) {
        return palabraService.save(palabra);
    }

    // Crear una nueva palabra con sus definiciones
    @PostMapping("/condefiniciones")
    public ResponseEntity<Palabra> createPalabraWithDefiniciones(@RequestBody Palabra palabra) {
        Palabra savedPalabra = palabraService.save(palabra);
        for (Definicion definicion : palabra.getDefiniciones()) {
            definicion.setPalabra(savedPalabra);
            definicionService.save(definicion);
        }
        return ResponseEntity.ok(savedPalabra);
    }

    // Actualizar una palabra existente
    @PutMapping("/{id}")
    public ResponseEntity<Palabra> updatePalabra(@PathVariable Long id, @RequestBody Palabra palabraDetails) {
        Optional<Palabra> palabra = palabraService.findById(id);
        if (palabra.isPresent()) {
            Palabra updatedPalabra = palabra.get();
            updatedPalabra.setTermino(palabraDetails.getTermino());
            updatedPalabra.setCategoriaGramatical(palabraDetails.getCategoriaGramatical());
            palabraService.save(updatedPalabra);
            return ResponseEntity.ok(updatedPalabra);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar una palabra y sus definiciones
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePalabra(@PathVariable Long id) {
        Optional<Palabra> palabra = palabraService.findById(id);
        if (palabra.isPresent()) {
            // Eliminar las definiciones asociadas
            palabra.get().getDefiniciones().forEach(definicion -> definicionService.deleteById(definicion.getId()));
            palabraService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener todas las definiciones de una palabra
    @GetMapping("/{id}/definiciones")
    public ResponseEntity<List<Definicion>> getDefinicionesByPalabraId(@PathVariable Long id) {
        Optional<Palabra> palabra = palabraService.findById(id);
        if (palabra.isPresent()) {
            return ResponseEntity.ok(palabra.get().getDefiniciones());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Agregar una nueva definición a una palabra
    @PostMapping("/{id}/definiciones")
    public ResponseEntity<Definicion> addDefinicionToPalabra(@PathVariable Long id, @RequestBody Definicion definicion) {
        Optional<Palabra> palabra = palabraService.findById(id);
        if (palabra.isPresent()) {
            definicion.setPalabra(palabra.get());
            Definicion savedDefinicion = definicionService.save(definicion);
            return ResponseEntity.ok(savedDefinicion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener una lista de palabras por inicial
    @GetMapping("/inicial/{letra}")
    public List<Palabra> getPalabrasByInicial(@PathVariable String letra) {
        return palabraService.findByInicial(letra);
    }

    // Obtener una lista de palabras por categoría gramatical
    @GetMapping("/categoria/{categoria}")
    public List<Palabra> getPalabrasByCategoria(@PathVariable String categoria) {
        return palabraService.findByCategoria(categoria);
    }
}
