package com.devshowcase.api.service;

import com.devshowcase.api.dto.request.ProfileRequestDTO;
import com.devshowcase.api.dto.response.ProfileResponseDTO;
import com.devshowcase.api.entity.Profile;
import com.devshowcase.api.exception.ResourceConflictException;
import com.devshowcase.api.exception.ResourceNotFoundException;
import com.devshowcase.api.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Transactional
    public ProfileResponseDTO createProfile(ProfileRequestDTO dto) {
        if (profileRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceConflictException("Já existe um perfil cadastrado com o e-mail: " + dto.getEmail());
        }

        Profile profile = new Profile();
        profile.setName(dto.getName());
        profile.setEmail(dto.getEmail());
        profile.setBio(dto.getBio());
        profile.setGithubUrl(dto.getGithubUrl());
        profile.setLinkedinUrl(dto.getLinkedinUrl());

        Profile savedProfile = profileRepository.save(profile);
        return ProfileResponseDTO.fromEntity(savedProfile);
    }

    @Transactional(readOnly = true)
    public ProfileResponseDTO getProfileById(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com o id: " + id));
        return ProfileResponseDTO.fromEntity(profile);
    }
}