package org.example.dictionaryapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dictionaryapp.dto.PalabraDTO;
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
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/palabras")
@Tag(name = "Palabras", description = "API para gestionar palabras y definiciones en el diccionario.")
public class PalabraController {

    @Autowired
    private DefinicionService definicionService;

    @Autowired
    private PalabraService palabraService;

    @Operation(summary = "Listar todas las palabras", description = "Devuelve una lista de todas las palabras registradas en el diccionario, sin incluir definiciones.")
    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<PalabraDTO>> getAllPalabras() {
        List<Palabra> list = palabraService.getAllPalabras();
        List<PalabraDTO> terminos = list.stream()
                .map(palabra -> new PalabraDTO(palabra.getId(), palabra.getTermino(), palabra.getCategoriaGramatical()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(terminos, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Listar todas las palabras con definiciones", description = "Obtiene todas las palabras junto con sus respectivas definiciones.")
    @CrossOrigin
    @GetMapping("/condefiniciones")
    public ResponseEntity<List<Palabra>> getAllPalabrasConDefiniciones() {
        List<Palabra> list = palabraService.getAllPalabras();
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Buscar una palabra por ID", description = "Obtiene los detalles de una palabra específica a partir de su ID.")
    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<PalabraDTO> getPalabraById(@PathVariable Long id) throws RecordNotFoundException {
        Palabra palabra = palabraService.getPalabraById(id);
        PalabraDTO termino = new PalabraDTO(palabra.getId(), palabra.getTermino(), palabra.getCategoriaGramatical());
        return new ResponseEntity<>(termino, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Buscar una palabra por ID con definiciones", description = "Obtiene una palabra específica junto con todas sus definiciones utilizando su ID.")
    @CrossOrigin
    @GetMapping("/{id}/condefiniciones")
    public ResponseEntity<Palabra> getPalabraByIdConDefiniciones(@PathVariable Long id) throws RecordNotFoundException {
        Palabra palabra = palabraService.getPalabraById(id);
        return new ResponseEntity<>(palabra, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Registrar una nueva palabra", description = "Agrega una nueva palabra al diccionario con su categoría gramatical.")
    @CrossOrigin
    @PostMapping
    public ResponseEntity<Palabra> createPalabra(@RequestBody Palabra palabra) {
        Palabra createdPalabra = palabraService.createPalabra(palabra);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPalabra);
    }

    @Operation(summary = "Actualizar una palabra existente", description = "Modifica los datos de una palabra registrada utilizando su ID.")
    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Palabra> updatePalabra(@PathVariable Long id, @RequestBody Palabra palabra) throws RecordNotFoundException {
        Palabra updatedPalabra = palabraService.updatePalabra(id, palabra);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPalabra);
    }

    @Operation(summary = "Eliminar una palabra", description = "Borra una palabra del diccionario mediante su ID.")
    @CrossOrigin
    @DeleteMapping("/{id}")
    public HttpStatus deletePalabra(@PathVariable Long id) throws RecordNotFoundException {
        palabraService.deletePalabra(id);
        return HttpStatus.ACCEPTED;
    }

    @Operation(summary = "Buscar palabras por categoría gramatical", description = "Busca palabras que pertenecen a una categoría gramatical específica.")
    @CrossOrigin
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<PalabraDTO>> findByCategoriaGramatical(@PathVariable String categoria) {
        List<Palabra> palabras = palabraService.findByCategoriaGramatical(categoria);
        List<PalabraDTO> result = palabras.stream()
                .map(palabra -> new PalabraDTO(palabra.getId(), palabra.getTermino(), palabra.getCategoriaGramatical()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Buscar palabras que empiezan con un término", description = "Busca palabras cuyo término empiece con una letra específica.")
    @CrossOrigin
    @GetMapping("/inicio/{inicial}")
    public ResponseEntity<List<PalabraDTO>> findByTerminoStartingWith(@PathVariable char inicial) {
        List<Palabra> palabras = palabraService.findByTerminoStartingWith(inicial);
        List<PalabraDTO> result = palabras.stream()
                .map(palabra -> new PalabraDTO(palabra.getId(), palabra.getTermino(), palabra.getCategoriaGramatical()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Verificar existencia de una palabra por término", description = "Verifica si una palabra existe en el diccionario usando su término.")
    @CrossOrigin
    @GetMapping("/existe/{termino}")
    public ResponseEntity<Boolean> existsByTermino(@PathVariable String termino) {
        boolean exists = palabraService.existsByTermino(termino);
        return new ResponseEntity<>(exists, new HttpHeaders(), HttpStatus.OK);
    }

}
