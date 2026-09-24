package com.devshowcase.api.controller;

import com.devshowcase.api.dto.request.ProjectRequestDTO;
import com.devshowcase.api.dto.response.ProjectResponseDTO;
import com.devshowcase.api.service.ProjectService;
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
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody ProjectRequestDTO dto) {
        ProjectResponseDTO createdProject = projectService.createProject(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        List<ProjectResponseDTO> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }
}
