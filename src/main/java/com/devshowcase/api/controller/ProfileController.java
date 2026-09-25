package com.devshowcase.api.controller;

import com.devshowcase.api.dto.request.ProfileRequestDTO;
import com.devshowcase.api.dto.response.ProfileResponseDTO;
import com.devshowcase.api.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Profiles", description = "Endpoints para gerenciamento de perfis de desenvolvedores")
@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Operation(summary = "Cadastrar perfil", description = "Cria um novo perfil de desenvolvedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    @PostMapping
    public ResponseEntity<ProfileResponseDTO> createProfile(@Valid @RequestBody ProfileRequestDTO dto) {
        ProfileResponseDTO createdProfile = profileService.createProfile(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
    }

    @Operation(summary = "Buscar perfil por ID", description = "Retorna os detalhes de um perfil existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponseDTO> getProfileById(@PathVariable Long id) {
        ProfileResponseDTO profile = profileService.getProfileById(id);
        return ResponseEntity.ok(profile);
    }
}
