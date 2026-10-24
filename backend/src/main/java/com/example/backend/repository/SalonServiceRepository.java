package com.example.backend.repository;

import com.example.backend.model.entity.SalonService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalonServiceRepository
        extends JpaRepository<SalonService, Long> {

    List<SalonService> findAllByOrderByNameAsc();

    List<SalonService> findByCategory_IdOrderByNameAsc(Long categoryId);
}
