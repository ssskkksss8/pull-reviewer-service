package com.example.demo.controller;

import com.example.demo.model.dto.PullRequestStatsResponse;
import com.example.demo.model.dto.ReviewerStatsResponse;
import com.example.demo.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/reviewers")
    public List<ReviewerStatsResponse> reviewerStats() {
        return statsService.getReviewerStats();
    }


    @GetMapping("/pull-requests")
    public List<PullRequestStatsResponse> pullRequestStats() {
        return statsService.getPullRequestStats();
    }
}
