package org.example.dictionaryapp.controller;

import org.example.dictionaryapp.model.Definicion;
import org.example.dictionaryapp.service.DefinicionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/definiciones")
public class DefinicionController {

    @Autowired
    private DefinicionService definicionService;

    // Obtener todas las definiciones
    @GetMapping
    public List<Definicion> getAllDefiniciones() {
        return definicionService.findAll();
    }

    // Obtener una definición por ID
    @GetMapping("/{id}")
    public ResponseEntity<Definicion> getDefinicionById(@PathVariable Long id) {
        Optional<Definicion> definicion = definicionService.findById(id);
        return definicion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    // Actualizar una definición existente
    @PutMapping("/{id}")
    public ResponseEntity<Definicion> updateDefinicion(@PathVariable Long id, @RequestBody Definicion definicionDetails) {
        Optional<Definicion> definicion = definicionService.findById(id);
        if (definicion.isPresent()) {
            Definicion updatedDefinicion = definicion.get();
            updatedDefinicion.setDescripcion(definicionDetails.getDescripcion());
            updatedDefinicion.setEjemplo(definicionDetails.getEjemplo());
            definicionService.save(updatedDefinicion);
            return ResponseEntity.ok(updatedDefinicion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar una definición específica
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDefinicion(@PathVariable Long id) {
        Optional<Definicion> definicion = definicionService.findById(id);
        if (definicion.isPresent()) {
            definicionService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}