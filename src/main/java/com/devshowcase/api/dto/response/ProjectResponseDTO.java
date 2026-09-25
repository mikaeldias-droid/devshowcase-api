package com.devshowcase.api.dto.response;

import com.devshowcase.api.entity.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String repositoryUrl;
    private String liveUrl;
    private Long profileId;
    private String profileName;
    private Integer upvotes = 0;
    private Double averageRating = 0.0;
    private List<TechnologyResponseDTO> technologies = new ArrayList<>();
    private List<FeedbackResponseDTO> feedbacks = new ArrayList<>();

    public ProjectResponseDTO() {
    }

    public ProjectResponseDTO(Long id, String title, String description, String repositoryUrl, String liveUrl, Long profileId, String profileName, Integer upvotes, Double averageRating, List<TechnologyResponseDTO> technologies, List<FeedbackResponseDTO> feedbacks) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.repositoryUrl = repositoryUrl;
        this.liveUrl = liveUrl;
        this.profileId = profileId;
        this.profileName = profileName;
        this.upvotes = upvotes != null ? upvotes : 0;
        this.averageRating = averageRating != null ? averageRating : 0.0;
        if (technologies != null) this.technologies = technologies;
        if (feedbacks != null) this.feedbacks = feedbacks;
    }

    public static ProjectResponseDTO fromEntity(Project entity) {
        if (entity == null) return null;

        List<TechnologyResponseDTO> techDtos = entity.getTechnologies() != null
                ? entity.getTechnologies().stream().map(TechnologyResponseDTO::fromEntity).collect(Collectors.toList())
                : new ArrayList<>();

        List<FeedbackResponseDTO> feedbackDtos = entity.getFeedbacks() != null
                ? entity.getFeedbacks().stream().map(FeedbackResponseDTO::fromEntity).collect(Collectors.toList())
                : new ArrayList<>();

        Long profileId = entity.getProfile() != null ? entity.getProfile().getId() : null;
        String profileName = entity.getProfile() != null ? entity.getProfile().getName() : null;

        return new ProjectResponseDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getRepositoryUrl(),
                entity.getLiveUrl(),
                profileId,
                profileName,
                entity.getUpvotes(),
                entity.getAverageRating(),
                techDtos,
                feedbackDtos
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public List<TechnologyResponseDTO> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<TechnologyResponseDTO> technologies) {
        this.technologies = technologies;
    }

    public List<FeedbackResponseDTO> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<FeedbackResponseDTO> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public Integer getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(Integer upvotes) {
        this.upvotes = upvotes;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}
