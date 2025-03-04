package org.example.dictionaryapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dictionaryapp.exception.RecordNotFoundException;
import org.example.dictionaryapp.service.DefinicionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/definiciones")
@Tag(name = "Definiciones")
public class DefinicionController {

    @Autowired
    private DefinicionService definicionService;

    @Operation(summary = "Eliminar una definición por su id")
    @CrossOrigin
    @DeleteMapping("/{id}")
    public HttpStatus deleteDefinicion(@PathVariable Long id) throws RecordNotFoundException {
        definicionService.deleteDefinicion(id);
        return HttpStatus.ACCEPTED;
    }
}