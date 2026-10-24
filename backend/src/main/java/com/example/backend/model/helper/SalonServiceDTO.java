package com.example.backend.model.helper;

import lombok.Data;

@Data
public class SalonServiceDTO {
    private Long id;
    private Long categoryId;
    private String name;
    private int durationMinutes;


    public SalonServiceDTO(Long id, Long categoryId, String name, String name1, int durationMinutes) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.durationMinutes = durationMinutes;
    }
}
