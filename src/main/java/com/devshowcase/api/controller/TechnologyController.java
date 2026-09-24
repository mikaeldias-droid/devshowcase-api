package com.devshowcase.api.controller;

import com.devshowcase.api.dto.request.TechnologyRequestDTO;
import com.devshowcase.api.dto.response.TechnologyResponseDTO;
import com.devshowcase.api.service.TechnologyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/technologies")
public class TechnologyController {

    private final TechnologyService technologyService;

    public TechnologyController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    @PostMapping
    public ResponseEntity<TechnologyResponseDTO> createTechnology(@Valid @RequestBody TechnologyRequestDTO dto) {
        TechnologyResponseDTO createdTechnology = technologyService.createTechnology(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTechnology);
    }

    @GetMapping
    public ResponseEntity<List<TechnologyResponseDTO>> getAllTechnologies() {
        List<TechnologyResponseDTO> technologies = technologyService.getAllTechnologies();
        return ResponseEntity.ok(technologies);
    }
}
