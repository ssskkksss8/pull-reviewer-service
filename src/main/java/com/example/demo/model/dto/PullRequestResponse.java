package com.example.demo.model.dto;

import com.example.demo.model.entity.PullRequest;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder

public class PullRequestResponse {
    private UUID id;
    private String title;
    private UUID authorId;
    private PullRequest.Status status;
    private Instant createdAt;
    private List<UUID> reviewerIds;
}
