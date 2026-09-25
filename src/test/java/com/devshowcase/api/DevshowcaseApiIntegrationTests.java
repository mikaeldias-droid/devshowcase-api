package com.devshowcase.api;

import com.devshowcase.api.dto.request.FeedbackRequestDTO;
import com.devshowcase.api.dto.request.ProfileRequestDTO;
import com.devshowcase.api.dto.request.ProjectRequestDTO;
import com.devshowcase.api.dto.request.TechnologyRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.config.location=classpath:/application-test.properties")
@AutoConfigureMockMvc
public class DevshowcaseApiIntegrationTests {

    @Autowired
    private org.springframework.test.web.servlet.MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testProfileFlow() throws Exception {
        ProfileRequestDTO request = new ProfileRequestDTO(
                "Maria Silva",
                "maria.silva@example.com",
                "Desenvolvedora Backend",
                "https://github.com/mariasilva",
                "https://linkedin.com/in/mariasilva"
        );

        // POST /api/profiles -> 201 Created
        String responseContent = mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Maria Silva"))
                .andExpect(jsonPath("$.email").value("maria.silva@example.com"))
                .andReturn().getResponse().getContentAsString();

        Long profileId = objectMapper.readTree(responseContent).get("id").asLong();

        // GET /api/profiles/{id} -> 200 OK
        mockMvc.perform(get("/api/profiles/" + profileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(profileId))
                .andExpect(jsonPath("$.name").value("Maria Silva"));

        // GET /api/profiles/{id} -> 404 Not Found
        mockMvc.perform(get("/api/profiles/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        // Duplicate Email -> 409 Conflict
        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));

        // Invalid Email & Invalid URL -> 400 Bad Request
        ProfileRequestDTO invalidRequest = new ProfileRequestDTO(
                "",
                "invalid-email",
                "Bio",
                "invalid-url",
                "https://linkedin.com"
        );
        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void testTechnologyFlow() throws Exception {
        TechnologyRequestDTO request = new TechnologyRequestDTO("Spring Boot", "Backend");

        // POST /api/technologies -> 201 Created
        mockMvc.perform(post("/api/technologies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Spring Boot"));

        // GET /api/technologies -> 200 OK
        mockMvc.perform(get("/api/technologies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // Duplicate Technology -> 409 Conflict
        mockMvc.perform(post("/api/technologies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        // Invalid Technology -> 400 Bad Request
        TechnologyRequestDTO invalidTech = new TechnologyRequestDTO("", "Category");
        mockMvc.perform(post("/api/technologies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTech)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testProjectFlow() throws Exception {
        // Create Profile first
        ProfileRequestDTO profileReq = new ProfileRequestDTO("João Souza", "joao@example.com", "Dev", "https://github.com/joao", null);
        String profileRes = mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long profileId = objectMapper.readTree(profileRes).get("id").asLong();

        // Create Technology
        TechnologyRequestDTO techReq = new TechnologyRequestDTO("Java", "Backend");
        String techRes = mockMvc.perform(post("/api/technologies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(techReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long techId = objectMapper.readTree(techRes).get("id").asLong();

        // Create Project
        ProjectRequestDTO projectReq = new ProjectRequestDTO(
                "DevShowcase API",
                "API em Spring Boot para cadastro de projetos",
                "https://github.com/joao/devshowcase-api",
                "https://devshowcase.api.example.com",
                profileId,
                Set.of(techId)
        );

        // POST /api/projects -> 201 Created
        String projectRes = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("DevShowcase API"))
                .andExpect(jsonPath("$.profileId").value(profileId))
                .andExpect(jsonPath("$.technologies[0].name").value("Java"))
                .andExpect(jsonPath("$.upvotes").value(0))
                .andExpect(jsonPath("$.averageRating").value(0.0))
                .andReturn().getResponse().getContentAsString();

        Long projectId = objectMapper.readTree(projectRes).get("id").asLong();

        // GET /api/projects (Paginated) -> 200 OK
        mockMvc.perform(get("/api/projects?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").exists());

        // GET /api/projects/{id} -> 200 OK
        mockMvc.perform(get("/api/projects/" + projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectId))
                .andExpect(jsonPath("$.title").value("DevShowcase API"));

        // GET /api/projects/99999 -> 404 Not Found
        mockMvc.perform(get("/api/projects/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Projeto não encontrado com o id: 99999"));

        // GET /api/projects with Technology filter -> 200 OK
        mockMvc.perform(get("/api/projects?technologyId=" + techId + "&page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        // Project with non-existent Profile -> 404 Not Found
        ProjectRequestDTO orphanProject = new ProjectRequestDTO(
                "Projeto Orfão",
                "Descrição",
                null,
                null,
                99999L,
                Set.of()
        );
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orphanProject)))
                .andExpect(status().isNotFound());

        // Upvote -> PUT /api/projects/{id}/upvote -> 200 OK
        mockMvc.perform(put("/api/projects/" + projectId + "/upvote"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.upvotes").value(1));

        // Upvote non-existent project -> 404
        mockMvc.perform(put("/api/projects/99999/upvote"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        // Add Feedback -> POST /api/projects/{id}/feedbacks -> 201 Created
        FeedbackRequestDTO feedbackReq = new FeedbackRequestDTO("Avaliador", "Excelente projeto!", 5);
        mockMvc.perform(post("/api/projects/" + projectId + "/feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(feedbackReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.comment").value("Excelente projeto!"));

        // Add Feedback invalid rating (>5) -> 400 Bad Request
        FeedbackRequestDTO invalidFeedbackReq = new FeedbackRequestDTO("Avaliador", "Nota inválida", 10);
        mockMvc.perform(post("/api/projects/" + projectId + "/feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidFeedbackReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        // Add Feedback non-existent project -> 404 Not Found
        mockMvc.perform(post("/api/projects/99999/feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(feedbackReq)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void testOpenApiDocsAvailable() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }
}
