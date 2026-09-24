package com.devshowcase.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.HashSet;
import java.util.Set;

public class ProjectRequestDTO {

    @NotBlank(message = "O título do projeto é obrigatório")
    private String title;

    @NotBlank(message = "A descrição do projeto é obrigatória")
    private String description;

    @Pattern(regexp = "^$|^(https?://)[^\\s]+$", message = "repositoryUrl deve ser uma URL válida com http:// ou https://")
    private String repositoryUrl;

    @Pattern(regexp = "^$|^(https?://)[^\\s]+$", message = "liveUrl deve ser uma URL válida com http:// ou https://")
    private String liveUrl;

    @NotNull(message = "O ID do perfil é obrigatório")
    private Long profileId;

    private Set<Long> technologyIds = new HashSet<>();

    public ProjectRequestDTO() {
    }

    public ProjectRequestDTO(String title, String description, String repositoryUrl, String liveUrl, Long profileId, Set<Long> technologyIds) {
        this.title = title;
        this.description = description;
        this.repositoryUrl = repositoryUrl;
        this.liveUrl = liveUrl;
        this.profileId = profileId;
        if (technologyIds != null) {
            this.technologyIds = technologyIds;
        }
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRepositoryUrl() { return repositoryUrl; }
    public void setRepositoryUrl(String repositoryUrl) { this.repositoryUrl = repositoryUrl; }
    public String getLiveUrl() { return liveUrl; }
    public void setLiveUrl(String liveUrl) { this.liveUrl = liveUrl; }
    public Long getProfileId() { return profileId; }
    public void setProfileId(Long profileId) { this.profileId = profileId; }
    public Set<Long> getTechnologyIds() { return technologyIds; }
    public void setTechnologyIds(Set<Long> technologyIds) { this.technologyIds = technologyIds; }
}