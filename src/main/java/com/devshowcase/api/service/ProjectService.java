package com.devshowcase.api.service;

import com.devshowcase.api.dto.request.ProjectRequestDTO;
import com.devshowcase.api.dto.response.ProjectResponseDTO;
import com.devshowcase.api.entity.Profile;
import com.devshowcase.api.entity.Project;
import com.devshowcase.api.entity.Technology;
import com.devshowcase.api.exception.ResourceNotFoundException;
import com.devshowcase.api.repository.ProfileRepository;
import com.devshowcase.api.repository.ProjectRepository;
import com.devshowcase.api.repository.TechnologyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProfileRepository profileRepository;
    private final TechnologyRepository technologyRepository;

    public ProjectService(ProjectRepository projectRepository,
                          ProfileRepository profileRepository,
                          TechnologyRepository technologyRepository) {
        this.projectRepository = projectRepository;
        this.profileRepository = profileRepository;
        this.technologyRepository = technologyRepository;
    }

    @Transactional
    public ProjectResponseDTO createProject(ProjectRequestDTO dto) {
        Profile profile = profileRepository.findById(dto.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com o id: " + dto.getProfileId()));

        Set<Technology> technologies = new HashSet<>();
        if (dto.getTechnologyIds() != null && !dto.getTechnologyIds().isEmpty()) {
            List<Technology> foundTechnologies = technologyRepository.findAllById(dto.getTechnologyIds());
            if (foundTechnologies.size() != dto.getTechnologyIds().size()) {
                Set<Long> foundIds = foundTechnologies.stream().map(Technology::getId).collect(Collectors.toSet());
                Set<Long> missingIds = dto.getTechnologyIds().stream()
                        .filter(id -> !foundIds.contains(id))
                        .collect(Collectors.toSet());
                throw new ResourceNotFoundException("Tecnologias não encontradas com os ids: " + missingIds);
            }
            technologies.addAll(foundTechnologies);
        }

        Project project = new Project();
        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());
        project.setRepositoryUrl(dto.getRepositoryUrl());
        project.setLiveUrl(dto.getLiveUrl());
        project.setProfile(profile);
        project.setTechnologies(technologies);

        Project savedProject = projectRepository.save(project);
        return ProjectResponseDTO.fromEntity(savedProject);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(ProjectResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}