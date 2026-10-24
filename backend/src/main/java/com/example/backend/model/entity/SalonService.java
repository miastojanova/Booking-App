package com.example.backend.model.entity;


import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Table(
        name = "service",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"category_id", "name"})
        }
)
public class SalonService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private ServiceCategory category;

    @Setter
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Setter
    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    protected SalonService() {
    }

    public SalonService(
            ServiceCategory category,
            String name,
            int durationMinutes
    ) {
        this.category = category;
        this.name = name;
        this.durationMinutes = durationMinutes;
    }

    public Long getId() {
        return id;
    }

    public ServiceCategory getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

}
