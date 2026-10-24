package com.example.backend.service.interfaces;

import com.example.backend.model.helper.WorkingHoursDTO;

import java.time.LocalDate;
import java.util.List;

public interface WorkingHoursService {
    WorkingHoursDTO getWorkingHours(LocalDate day);
    WorkingHoursDTO updateWorkingHours(WorkingHoursDTO dto);
    List<WorkingHoursDTO> getAllSchedules();
}
