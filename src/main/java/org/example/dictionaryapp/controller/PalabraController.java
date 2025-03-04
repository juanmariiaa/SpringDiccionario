package org.example.dictionaryapp.controller;

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
public class PalabraController {

    @Autowired
    private DefinicionService definicionService;

    @Autowired
    private PalabraService palabraService;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<PalabraDTO>> getAllPalabras() {
        List<Palabra> list = palabraService.getAllPalabras();
        List<PalabraDTO> terminos = list.stream()
                .map(palabra -> new PalabraDTO(palabra.getId(), palabra.getTermino(), palabra.getCategoriaGramatical()))
                .collect(Collectors.toList());
        return new ResponseEntity<List<PalabraDTO>>(terminos, new HttpHeaders(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/condefiniciones")
    public ResponseEntity<List<Palabra>> getAllPalabrasConDefiniciones() {
        List<Palabra> list = palabraService.getAllPalabras();
        return new ResponseEntity<List<Palabra>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<PalabraDTO> getPalabraById(@PathVariable Long id) throws RecordNotFoundException {
        Palabra palabra = palabraService.getPalabraById(id);
        PalabraDTO termino = new PalabraDTO(palabra.getId(), palabra.getTermino(), palabra.getCategoriaGramatical());
        return new ResponseEntity<PalabraDTO>(termino, new HttpHeaders(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/{id}/condefiniciones")
    public ResponseEntity<Palabra> getPalabraByIdConDefiniciones(@PathVariable Long id) throws RecordNotFoundException {
        Palabra palabra = palabraService.getPalabraById(id);
        return new ResponseEntity<Palabra>(palabra, new HttpHeaders(), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Palabra> createPalabra(@RequestBody Palabra palabra) {
        Palabra createdPalabra = palabraService.createPalabra(palabra);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPalabra);
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Palabra> updatePalabra(@PathVariable Long id, @RequestBody Palabra palabra) throws RecordNotFoundException {
        Palabra updatedPalabra = palabraService.updatePalabra(id, palabra);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPalabra);
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
    @PostMapping("/condefiniciones")
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

    @CrossOrigin
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> estadisticas = palabraService.obtenerEstadisticas();
        return new ResponseEntity<Map<String, Object>>(estadisticas, new HttpHeaders(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/exportar")
    public ResponseEntity<String> exportarDiccionario() {
        String exportado = palabraService.exportarDiccionario();
        return ResponseEntity.status(HttpStatus.OK).body(exportado);
    }

}