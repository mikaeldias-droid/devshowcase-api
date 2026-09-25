package com.devshowcase.api.controller;

import com.devshowcase.api.dto.request.TechnologyRequestDTO;
import com.devshowcase.api.dto.response.TechnologyResponseDTO;
import com.devshowcase.api.service.TechnologyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Technologies", description = "Endpoints para gerenciamento de tecnologias")
@RestController
@RequestMapping("/api/technologies")
public class TechnologyController {

    private final TechnologyService technologyService;

    public TechnologyController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    @Operation(summary = "Cadastrar tecnologia", description = "Cadastra uma nova tecnologia no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tecnologia criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Tecnologia já existente")
    })
    @PostMapping
    public ResponseEntity<TechnologyResponseDTO> createTechnology(@Valid @RequestBody TechnologyRequestDTO dto) {
        TechnologyResponseDTO createdTechnology = technologyService.createTechnology(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTechnology);
    }

    @Operation(summary = "Listar tecnologias", description = "Retorna a lista de todas as tecnologias cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tecnologias obtida com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<TechnologyResponseDTO>> getAllTechnologies() {
        List<TechnologyResponseDTO> technologies = technologyService.getAllTechnologies();
        return ResponseEntity.ok(technologies);
    }
}
