package com.devshowcase.api.dto.response;

import com.devshowcase.api.entity.Technology;

public class TechnologyResponseDTO {

    private Long id;
    private String name;
    private String category;

    public TechnologyResponseDTO() {
    }

    public TechnologyResponseDTO(Long id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public static TechnologyResponseDTO fromEntity(Technology entity) {
        if (entity == null) return null;
        return new TechnologyResponseDTO(entity.getId(), entity.getName(), entity.getCategory());
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
