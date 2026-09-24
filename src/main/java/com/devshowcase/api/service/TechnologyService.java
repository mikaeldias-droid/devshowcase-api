package com.devshowcase.api.service;

import com.devshowcase.api.dto.request.TechnologyRequestDTO;
import com.devshowcase.api.dto.response.TechnologyResponseDTO;
import com.devshowcase.api.entity.Technology;
import com.devshowcase.api.exception.ResourceConflictException;
import com.devshowcase.api.repository.TechnologyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TechnologyService {

    private final TechnologyRepository technologyRepository;

    public TechnologyService(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    @Transactional
    public TechnologyResponseDTO createTechnology(TechnologyRequestDTO dto) {
        if (technologyRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new ResourceConflictException("Já existe uma tecnologia cadastrada com o nome: " + dto.getName());
        }

        Technology technology = new Technology();
        technology.setName(dto.getName());
        technology.setCategory(dto.getCategory());

        Technology savedTechnology = technologyRepository.save(technology);
        return TechnologyResponseDTO.fromEntity(savedTechnology);
    }

    @Transactional(readOnly = true)
    public List<TechnologyResponseDTO> getAllTechnologies() {
        return technologyRepository.findAll()
                .stream()
                .map(TechnologyResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}