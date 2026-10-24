package com.example.backend.controller;

import com.example.backend.model.helper.CategoryDTO;
import com.example.backend.model.helper.SalonServiceDTO;
import com.example.backend.service.impl.SalonServiceServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/categories")
public class CategoryController {
    private final SalonServiceServiceImpl serviceService;

    public CategoryController(SalonServiceServiceImpl serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping
    public List<CategoryDTO> getCategories() {
        return serviceService.getCategories();
    }
}
