package com.devshowcase.api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FeedbackRequestDTO {

    private String authorName;

    @NotBlank(message = "O comentário é obrigatório")
    private String comment;

    @NotNull(message = "A nota é obrigatória")
    @Min(value = 1, message = "A nota deve ser no mínimo 1")
    @Max(value = 5, message = "A nota deve ser no máximo 5")
    private Integer rating;

    public FeedbackRequestDTO() {
    }

    public FeedbackRequestDTO(String authorName, String comment, Integer rating) {
        this.authorName = authorName;
        this.comment = comment;
        this.rating = rating;
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
