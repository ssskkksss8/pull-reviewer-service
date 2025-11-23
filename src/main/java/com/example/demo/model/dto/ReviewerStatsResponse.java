package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ReviewerStatsResponse {
    private UUID reviewerId;
    private long assignmentsCount;
}
