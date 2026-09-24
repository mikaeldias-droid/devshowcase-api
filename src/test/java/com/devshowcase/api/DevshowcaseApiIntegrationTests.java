package com.devshowcase.api;

import com.devshowcase.api.dto.request.ProfileRequestDTO;
import com.devshowcase.api.dto.request.ProjectRequestDTO;
import com.devshowcase.api.dto.request.TechnologyRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("DevShowcase API"))
                .andExpect(jsonPath("$.profileId").value(profileId))
                .andExpect(jsonPath("$.technologies[0].name").value("Java"));

        // GET /api/projects -> 200 OK
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

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
    }
}
