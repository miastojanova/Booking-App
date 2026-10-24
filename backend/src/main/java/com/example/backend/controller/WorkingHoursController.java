package com.example.backend.controller;

import com.example.backend.model.helper.WorkingHoursDTO;
import com.example.backend.service.interfaces.WorkingHoursService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/working-hours")
public class WorkingHoursController {
    private final WorkingHoursService workingHoursService;
    public WorkingHoursController(WorkingHoursService workingHoursService) {
        this.workingHoursService = workingHoursService;
    }

    @GetMapping
    public WorkingHoursDTO getWorkingHours(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return workingHoursService.getWorkingHours(date);
    }

    @GetMapping("/all")
    public List<WorkingHoursDTO> getAllSchedules() {
        return workingHoursService.getAllSchedules();
    }

    @PutMapping
    public WorkingHoursDTO updateWorkingHours(
            @RequestBody WorkingHoursDTO dto
    ) {
        return workingHoursService.updateWorkingHours(dto);
    }
}
