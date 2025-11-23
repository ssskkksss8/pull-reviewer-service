package com.example.demo.service;

import com.example.demo.model.dto.PullRequestStatsResponse;
import com.example.demo.model.dto.ReviewerStatsResponse;
import com.example.demo.repository.ReviewerAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final ReviewerAssignmentRepository reviewerAssignmentRepository;

    public List<ReviewerStatsResponse> getReviewerStats() {
        return reviewerAssignmentRepository.countAssignmentsByReviewer();
    }

    public List<PullRequestStatsResponse> getPullRequestStats() {
        return reviewerAssignmentRepository.countReviewersByPullRequest();
    }
}
