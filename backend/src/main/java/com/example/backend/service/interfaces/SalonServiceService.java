package com.example.backend.service.interfaces;

import com.example.backend.model.entity.SalonService;
import com.example.backend.model.helper.CategoryDTO;
import com.example.backend.model.helper.SalonServiceDTO;

import java.util.List;

public interface SalonServiceService {
    List<CategoryDTO> getCategories();
    List<SalonServiceDTO> getServicesByCategoryId(Long categoryId);
    SalonServiceDTO getSalonService(Long serviceId);
    SalonServiceDTO toDTO(SalonService salonService);
}
