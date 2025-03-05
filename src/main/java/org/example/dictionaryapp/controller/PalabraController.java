package org.example.dictionaryapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dictionaryapp.dto.PalabraDTO;
import org.example.dictionaryapp.exception.RecordNotFoundException;
import org.example.dictionaryapp.model.Palabra;
import org.example.dictionaryapp.model.Definicion;
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
    private PalabraService palabraService;

    @Operation(summary = "Listar todas las palabras", description = "Devuelve una lista de todas las palabras registradas en el diccionario, sin incluir definiciones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de palabras obtenida exitosamente."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de palabras con definiciones obtenida exitosamente."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @CrossOrigin
    @GetMapping("/condefiniciones")
    public ResponseEntity<List<Palabra>> getAllDiccionario() {
        List<Palabra> list = palabraService.getAllPalabras();
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Buscar una palabra por ID", description = "Obtiene los detalles de una palabra específica a partir de su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Palabra encontrada exitosamente."),
            @ApiResponse(responseCode = "404", description = "Palabra no encontrada para el ID proporcionado."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<PalabraDTO> getPalabraById(@PathVariable Long id) throws RecordNotFoundException {
        Palabra palabra = palabraService.getPalabraById(id);
        PalabraDTO termino = new PalabraDTO(palabra.getId(), palabra.getTermino(), palabra.getCategoriaGramatical());
        return new ResponseEntity<>(termino, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Buscar una palabra por ID con definiciones", description = "Obtiene una palabra específica junto con todas sus definiciones utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Palabra con definiciones encontrada exitosamente."),
            @ApiResponse(responseCode = "404", description = "Palabra no encontrada para el ID proporcionado."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @CrossOrigin
    @GetMapping("/{id}/condefiniciones")
    public ResponseEntity<Palabra> getPalabraByIdConDefiniciones(@PathVariable Long id) throws RecordNotFoundException {
        Palabra palabra = palabraService.getPalabraById(id);
        return new ResponseEntity<>(palabra, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Registrar una nueva palabra", description = "Agrega una nueva palabra al diccionario con su categoría gramatical.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Palabra creada exitosamente."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej: término o categoría vacía)."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @CrossOrigin
    @PostMapping
    public ResponseEntity<Palabra> createPalabra(@RequestBody Palabra palabra) {
        Palabra createdPalabra = palabraService.createPalabra(palabra);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPalabra);
    }

    @Operation(summary = "Actualizar una palabra existente", description = "Modifica los datos de una palabra registrada utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Palabra actualizada exitosamente."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej: término o categoría vacía)."),
            @ApiResponse(responseCode = "404", description = "Palabra no encontrada para el ID proporcionado."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Palabra> updatePalabra(@PathVariable Long id, @RequestBody Palabra palabra) throws RecordNotFoundException {
        Palabra updatedPalabra = palabraService.updatePalabra(id, palabra);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPalabra);
    }

    @Operation(summary = "Eliminar una palabra", description = "Borra una palabra del diccionario mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Palabra eliminada exitosamente."),
            @ApiResponse(responseCode = "404", description = "Palabra no encontrada para el ID proporcionado."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @CrossOrigin
    @DeleteMapping("/{id}")
    public HttpStatus deletePalabra(@PathVariable Long id) throws RecordNotFoundException {
        palabraService.deletePalabra(id);
        return HttpStatus.ACCEPTED;
    }

    @Operation(summary = "Buscar palabras por categoría gramatical", description = "Busca palabras que pertenecen a una categoría gramatical específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de palabras obtenida exitosamente."),
            @ApiResponse(responseCode = "400", description = "Categoría gramatical no proporcionada o inválida."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de palabras obtenida exitosamente."),
            @ApiResponse(responseCode = "400", description = "Letra inicial no proporcionada o inválida."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Existencia de la palabra verificada exitosamente."),
            @ApiResponse(responseCode = "400", description = "Término no proporcionado o inválido."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @CrossOrigin
    @GetMapping("/existe/{termino}")
    public ResponseEntity<Boolean> existsByTermino(@PathVariable String termino) {
        boolean exists = palabraService.existsByTermino(termino);
        return new ResponseEntity<>(exists, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Exportar diccionario a CSV", description = "Exporta todas las palabras y sus definiciones a un formato CSV.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Diccionario exportado exitosamente."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @CrossOrigin
    @GetMapping("/exportar")
    public ResponseEntity<String> exportarDiccionario() {
        String csvContent = palabraService.exportarDiccionario();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=diccionario.csv");
        return new ResponseEntity<>(csvContent, headers, HttpStatus.OK);
    }

    @Operation(summary = "Obtener estadísticas del diccionario", description = "Obtiene estadísticas sobre el total de palabras, definiciones y categorías gramaticales en el diccionario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @CrossOrigin
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> estadisticas = palabraService.obtenerEstadisticas();
        return new ResponseEntity<>(estadisticas, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Registrar una nueva palabra con definiciones", description = "Agrega una nueva palabra al diccionario junto con sus definiciones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Palabra con definiciones creada exitosamente."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej: término, categoría o definiciones vacías)."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @CrossOrigin
    @PostMapping("/condefiniciones")
    public ResponseEntity<Palabra> createPalabraConDefiniciones(@RequestBody Map<String, Object> request) {
        Palabra palabra = new Palabra();
        palabra.setTermino((String) request.get("termino"));
        palabra.setCategoriaGramatical((String) request.get("categoriaGramatical"));

        List<Map<String, String>> definicionesData = (List<Map<String, String>>) request.get("definiciones");
        List<Definicion> definiciones = definicionesData.stream()
                .map(data -> {
                    Definicion definicion = new Definicion();
                    definicion.setDescripcion(data.get("descripcion"));
                    return definicion;
                })
                .collect(Collectors.toList());

        Palabra createdPalabra = palabraService.createPalabraConDefiniciones(palabra, definiciones);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPalabra);
    }

    @Operation(summary = "Agregar una nueva definición a una palabra", description = "Agrega una nueva definición a una palabra existente en el diccionario mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Definición agregada exitosamente."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej: descripción vacía)."),
            @ApiResponse(responseCode = "404", description = "Palabra no encontrada para el ID proporcionado."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @CrossOrigin
    @PostMapping("/{id}/definiciones")
    public ResponseEntity<Palabra> addDefinicionToPalabra(@PathVariable Long id, @RequestBody Definicion definicion) throws RecordNotFoundException {
        Palabra updatedPalabra = palabraService.addDefinicionToPalabra(id, definicion);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPalabra);
    }

    @Operation(summary = "Obtener todas las definiciones de una palabra", description = "Devuelve una lista de todas las definiciones asociadas a una palabra específica a partir de su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de definiciones obtenida exitosamente."),
            @ApiResponse(responseCode = "404", description = "Palabra no encontrada para el ID proporcionado."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @CrossOrigin
    @GetMapping("/{id}/definiciones")
    public ResponseEntity<List<Definicion>> getDefinicionesByPalabraId(@PathVariable Long id) throws RecordNotFoundException {
        List<Definicion> definiciones = palabraService.getDefinicionesByPalabraId(id);
        return new ResponseEntity<>(definiciones, new HttpHeaders(), HttpStatus.OK);
    }
}