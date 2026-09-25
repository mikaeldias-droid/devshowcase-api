package com.devshowcase.api.service;

import com.devshowcase.api.dto.request.FeedbackRequestDTO;
import com.devshowcase.api.dto.request.ProjectRequestDTO;
import com.devshowcase.api.dto.response.FeedbackResponseDTO;
import com.devshowcase.api.dto.response.ProjectResponseDTO;
import com.devshowcase.api.entity.Feedback;
import com.devshowcase.api.entity.Profile;
import com.devshowcase.api.entity.Project;
import com.devshowcase.api.entity.Technology;
import com.devshowcase.api.exception.ResourceNotFoundException;
import com.devshowcase.api.repository.FeedbackRepository;
import com.devshowcase.api.repository.ProfileRepository;
import com.devshowcase.api.repository.ProjectRepository;
import com.devshowcase.api.repository.TechnologyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final FeedbackRepository feedbackRepository;

    public ProjectService(ProjectRepository projectRepository,
                          ProfileRepository profileRepository,
                          TechnologyRepository technologyRepository,
                          FeedbackRepository feedbackRepository) {
        this.projectRepository = projectRepository;
        this.profileRepository = profileRepository;
        this.technologyRepository = technologyRepository;
        this.feedbackRepository = feedbackRepository;
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

    @Transactional(readOnly = true)
    public ProjectResponseDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado com o id: " + id));
        return ProjectResponseDTO.fromEntity(project);
    }

    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> getProjects(Long technologyId, Pageable pageable) {
        Page<Project> page;
        if (technologyId != null) {
            page = projectRepository.findByTechnologiesId(technologyId, pageable);
        } else {
            page = projectRepository.findAll(pageable);
        }
        return page.map(ProjectResponseDTO::fromEntity);
    }

    @Transactional
    public FeedbackResponseDTO addFeedback(Long projectId, FeedbackRequestDTO dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado com o id: " + projectId));

        String authorName = (dto.getAuthorName() != null && !dto.getAuthorName().isBlank())
                ? dto.getAuthorName()
                : "Anônimo";

        Feedback feedback = new Feedback();
        feedback.setAuthorName(authorName);
        feedback.setComment(dto.getComment());
        feedback.setRating(dto.getRating());
        feedback.setProject(project);

        Feedback savedFeedback = feedbackRepository.save(feedback);
        project.getFeedbacks().add(savedFeedback);

        // Recalculate average rating
        double avg = project.getFeedbacks().stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);
        
        // Round to 2 decimal places
        double roundedAvg = Math.round(avg * 100.0) / 100.0;
        project.setAverageRating(roundedAvg);
        projectRepository.save(project);

        return FeedbackResponseDTO.fromEntity(savedFeedback);
    }

    @Transactional
    public ProjectResponseDTO upvoteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado com o id: " + projectId));

        int currentUpvotes = project.getUpvotes() != null ? project.getUpvotes() : 0;
        project.setUpvotes(currentUpvotes + 1);

        Project updatedProject = projectRepository.save(project);
        return ProjectResponseDTO.fromEntity(updatedProject);
    }
}