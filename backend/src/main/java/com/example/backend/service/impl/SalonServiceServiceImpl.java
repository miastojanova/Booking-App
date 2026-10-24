package com.example.backend.service.impl;

import com.example.backend.model.entity.SalonService;
import com.example.backend.model.helper.CategoryDTO;
import com.example.backend.model.helper.SalonServiceDTO;
import com.example.backend.repository.SalonServiceRepository;
import com.example.backend.repository.ServiceCategoryRepository;
import com.example.backend.service.interfaces.SalonServiceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class SalonServiceServiceImpl implements SalonServiceService {
    private final ServiceCategoryRepository categoryRepository;
    private final SalonServiceRepository serviceRepository;

    @Override
    public List<CategoryDTO> getCategories() {
        return categoryRepository.findAllByOrderByNameAsc()
                .stream()
                .map(category -> new CategoryDTO(
                        category.getId(),
                        category.getName()
                ))
                .toList();
    }

    @Override
    public List<SalonServiceDTO> getServicesByCategoryId(Long categoryId) {
        List<SalonService> services;

        if (categoryId == null) {
            services = serviceRepository.findAllByOrderByNameAsc();
        } else {
            if (!categoryRepository.existsById(categoryId)) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Category not found."
                );
            }

            services = serviceRepository
                    .findByCategory_IdOrderByNameAsc(categoryId);
        }

        return services.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public SalonServiceDTO getSalonService(Long serviceId) {
        SalonService service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Service not found."
                ));

        return toDTO(service);
    }

    @Override
    public SalonServiceDTO toDTO(SalonService salonService) {
        return new SalonServiceDTO(
                salonService.getId(),
                salonService.getCategory().getId(),
                salonService.getName(),
                salonService.getName(),
                salonService.getDurationMinutes()
        );
    }
}