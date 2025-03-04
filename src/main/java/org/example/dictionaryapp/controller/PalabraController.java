package org.example.dictionaryapp.controller;

import org.example.dictionaryapp.exception.RecordNotFoundException;
import org.example.dictionaryapp.model.Definicion;
import org.example.dictionaryapp.model.Palabra;
import org.example.dictionaryapp.service.DefinicionService;
import org.example.dictionaryapp.service.PalabraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/palabras")
public class PalabraController {

    @Autowired
    private DefinicionService definicionService;

    @Autowired
    private PalabraService palabraService;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<Palabra>> getAllPalabras() {
        List<Palabra> list = palabraService.getAllPalabras();
        return new ResponseEntity<List<Palabra>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Palabra> getPalabraById(@PathVariable Long id) throws RecordNotFoundException {
        Palabra palabra = palabraService.getPalabraById(id);
        return new ResponseEntity<Palabra>(palabra, new HttpHeaders(), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping
    public ResponseEntity<Palabra> createPalabra(@RequestBody Palabra palabra) {
        Palabra createdPalabra = palabraService.createPalabra(palabra);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPalabra);
    }

    @CrossOrigin
    @PutMapping("/{id}") // Use an ID to identify the Palabra to update
    public ResponseEntity<Palabra> updatePalabra(@PathVariable Long id, @RequestBody Palabra palabra) throws RecordNotFoundException {
        Palabra updatedPalabra = palabraService.updatePalabra(id, palabra);
        return ResponseEntity.ok(updatedPalabra);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public HttpStatus deletePalabra(@PathVariable Long id) throws RecordNotFoundException {
        palabraService.deletePalabra(id);
        return HttpStatus.ACCEPTED;
    }

    @CrossOrigin
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Palabra>> getPalabrasByCategoria(@PathVariable String categoria) {
        List<Palabra> list = palabraService.getPalabrasByCategoria(categoria);
        return new ResponseEntity<List<Palabra>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/inicial/{letra}")
    public ResponseEntity<List<Palabra>> getPalabrasByInicial(@PathVariable char letra) {
        List<Palabra> list = palabraService.getPalabrasByInicial(letra);
        return new ResponseEntity<List<Palabra>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/existe/{termino}")
    public ResponseEntity<Boolean> existePalabra(@PathVariable String termino) {
        boolean existe = palabraService.existePalabra(termino);
        return new ResponseEntity<Boolean>(existe, new HttpHeaders(), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/condefiniciones")
    public ResponseEntity<Palabra> createPalabraConDefiniciones(@RequestBody Palabra palabra) {
        Palabra createdPalabra = palabraService.createPalabraConDefiniciones(palabra);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPalabra);
    }

    @CrossOrigin
    @GetMapping("/{id}/definiciones")
    public ResponseEntity<List<Definicion>> getDefinicionesByPalabraId(@PathVariable Long id) throws RecordNotFoundException {
        List<Definicion> list = definicionService.getDefinicionesByPalabraId(id);
        return new ResponseEntity<List<Definicion>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/{id}/definiciones")
    public ResponseEntity<Definicion> createDefinicion(@PathVariable Long id, @RequestBody Definicion definicion) throws RecordNotFoundException {
        Definicion createdDefinicion = definicionService.createDefinicion(id, definicion);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDefinicion);
    }
}