package com.example.backend.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private SalonService service;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Setter
    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Setter
    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    protected Booking() {
    }

    public Booking(
            SalonService service,
            Client client,
            LocalDateTime startAt,
            LocalDateTime endAt
    ) {
        this.service = service;
        this.client = client;
        this.startAt = startAt;
        this.endAt = endAt;
    }

}
