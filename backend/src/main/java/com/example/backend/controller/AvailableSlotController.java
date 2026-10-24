package com.example.backend.controller;

import com.example.backend.model.helper.AvailableSlotDTO;
import com.example.backend.service.interfaces.AvailableSlotService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/availability")
public class AvailableSlotController {
    private final AvailableSlotService availableSlotService;

    public AvailableSlotController(AvailableSlotService availableSlotService) {
        this.availableSlotService = availableSlotService;
    }

    @GetMapping
    public List<AvailableSlotDTO> getAvailableSlots(
            @RequestParam("serviceId") Long serviceId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return availableSlotService.getAvailableSlots(serviceId, date);
    }
}
