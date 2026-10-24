package com.example.backend.repository;

import com.example.backend.model.entity.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceCategoryRepository
        extends JpaRepository<ServiceCategory, Long> {

    List<ServiceCategory> findAllByOrderByNameAsc();
}
