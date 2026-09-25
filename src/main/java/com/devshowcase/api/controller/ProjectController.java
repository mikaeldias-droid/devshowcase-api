package com.devshowcase.api.controller;

import com.devshowcase.api.dto.request.FeedbackRequestDTO;
import com.devshowcase.api.dto.request.ProjectRequestDTO;
import com.devshowcase.api.dto.response.FeedbackResponseDTO;
import com.devshowcase.api.dto.response.ProjectResponseDTO;
import com.devshowcase.api.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Projects", description = "Endpoints para gerenciamento de projetos, feedbacks e upvotes")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Cadastrar projeto", description = "Cria um novo projeto vinculado a um perfil existente e tecnologias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Projeto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Perfil ou tecnologia não encontrada")
    })
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody ProjectRequestDTO dto) {
        ProjectResponseDTO createdProject = projectService.createProject(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @Operation(summary = "Listar projetos", description = "Lista projetos com suporte a paginação e filtro por tecnologia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista paginada de projetos recuperada com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<ProjectResponseDTO>> getProjects(
            @RequestParam(required = false) Long technologyId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<ProjectResponseDTO> projects = projectService.getProjects(technologyId, pageable);
        return ResponseEntity.ok(projects);
    }

    @Operation(summary = "Buscar projeto por ID", description = "Retorna os detalhes de um projeto existente pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {
        ProjectResponseDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @Operation(summary = "Cadastrar feedback no projeto", description = "Cadastra uma nota (1 a 5) e um comentário para o projeto, recalculando a nota média")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Feedback cadastrado com sucesso e nota média recalculada"),
            @ApiResponse(responseCode = "400", description = "Nota fora do intervalo 1-5 ou comentário em branco"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    @PostMapping("/{id}/feedbacks")
    public ResponseEntity<FeedbackResponseDTO> addFeedback(
            @PathVariable Long id,
            @Valid @RequestBody FeedbackRequestDTO dto) {
        FeedbackResponseDTO feedback = projectService.addFeedback(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(feedback);
    }

    @Operation(summary = "Incrementar curtidas (Upvote)", description = "Incrementa em 1 a quantidade de curtidas/upvotes do projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upvote registrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    @PutMapping("/{id}/upvote")
    public ResponseEntity<ProjectResponseDTO> upvoteProject(@PathVariable Long id) {
        ProjectResponseDTO updatedProject = projectService.upvoteProject(id);
        return ResponseEntity.ok(updatedProject);
    }
}
