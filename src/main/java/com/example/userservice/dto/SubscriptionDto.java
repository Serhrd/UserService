package com.example.userservice.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDto {
    private Long id;
    private String serviceName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long userId;
}