package com.devshowcase.api.dto.response;

import com.devshowcase.api.entity.Feedback;

public class FeedbackResponseDTO {

    private Long id;
    private String authorName;
    private String comment;
    private Integer rating;

    public FeedbackResponseDTO() {
    }

    public FeedbackResponseDTO(Long id, String authorName, String comment, Integer rating) {
        this.id = id;
        this.authorName = authorName;
        this.comment = comment;
        this.rating = rating;
    }

    public static FeedbackResponseDTO fromEntity(Feedback entity) {
        if (entity == null) return null;
        return new FeedbackResponseDTO(entity.getId(), entity.getAuthorName(), entity.getComment(), entity.getRating());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
