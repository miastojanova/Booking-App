package com.example.backend.controller;

import com.example.backend.model.helper.SalonServiceDTO;
import com.example.backend.service.impl.SalonServiceServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class SalonServiceController {
    private final SalonServiceServiceImpl salonService;
    public SalonServiceController(SalonServiceServiceImpl salonService) {
        this.salonService = salonService;
    }

    @GetMapping
    public List<SalonServiceDTO> getServices(
            @RequestParam(name = "categoryId", required = false)
            Long categoryId
    ) {
        return salonService.getServicesByCategoryId(categoryId);
    }

    @GetMapping("/{serviceId}")
    public SalonServiceDTO getService(
            @PathVariable Long serviceId
    ) {
        return salonService.getSalonService(serviceId);
    }
}
