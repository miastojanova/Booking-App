package com.example.backend.model.helper;

import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    private String name;

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
