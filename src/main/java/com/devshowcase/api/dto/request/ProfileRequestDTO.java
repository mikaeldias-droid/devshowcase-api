package com.devshowcase.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ProfileRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Informe um e-mail válido")
    private String email;

    private String bio;

    @Pattern(regexp = "^$|^(https?://)[^\\s]+$", message = "githubUrl deve ser uma URL válida com http:// ou https://")
    private String githubUrl;

    @Pattern(regexp = "^$|^(https?://)[^\\s]+$", message = "linkedinUrl deve ser uma URL válida com http:// ou https://")
    private String linkedinUrl;

    public ProfileRequestDTO() {
    }

    public ProfileRequestDTO(String name, String email, String bio, String githubUrl, String linkedinUrl) {
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.githubUrl = githubUrl;
        this.linkedinUrl = linkedinUrl;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getGithubUrl() { return githubUrl; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }
    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }
}