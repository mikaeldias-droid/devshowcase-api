package com.devshowcase.api.dto.response;

import com.devshowcase.api.entity.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String bio;
    private String githubUrl;
    private String linkedinUrl;
    private List<ProjectResponseDTO> projects = new ArrayList<>();

    public ProfileResponseDTO() {
    }

    public ProfileResponseDTO(Long id, String name, String email, String bio, String githubUrl, String linkedinUrl, List<ProjectResponseDTO> projects) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.githubUrl = githubUrl;
        this.linkedinUrl = linkedinUrl;
        if (projects != null) this.projects = projects;
    }

    public static ProfileResponseDTO fromEntity(Profile entity) {
        if (entity == null) return null;

        List<ProjectResponseDTO> projectDtos = entity.getProjects() != null
                ? entity.getProjects().stream().map(ProjectResponseDTO::fromEntity).collect(Collectors.toList())
                : new ArrayList<>();

        return new ProfileResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getBio(),
                entity.getGithubUrl(),
                entity.getLinkedinUrl(),
                projectDtos
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public List<ProjectResponseDTO> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectResponseDTO> projects) {
        this.projects = projects;
    }
}
