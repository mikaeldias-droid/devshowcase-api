package com.devshowcase.api.controller;

import com.devshowcase.api.dto.request.ProfileRequestDTO;
import com.devshowcase.api.dto.response.ProfileResponseDTO;
import com.devshowcase.api.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public ResponseEntity<ProfileResponseDTO> createProfile(@Valid @RequestBody ProfileRequestDTO dto) {
        ProfileResponseDTO createdProfile = profileService.createProfile(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponseDTO> getProfileById(@PathVariable Long id) {
        ProfileResponseDTO profile = profileService.getProfileById(id);
        return ResponseEntity.ok(profile);
    }
}
