package com.example.backend.model.helper;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDTO {
    private Long id;
    private Long serviceId;
    private String serviceName;
    private String clientName;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public BookingDTO(Long id,
                      Long serviceId,
                      String serviceName,
                      String clientName,
                      LocalDateTime startAt,
                      LocalDateTime endAt) {
        this.id = id;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.clientName = clientName;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
