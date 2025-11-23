package com.example.demo.controller;

import com.example.demo.model.dto.CreatePullRequestRequest;
import com.example.demo.model.dto.PullRequestResponse;
import com.example.demo.service.PullRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pull-request")
@RequiredArgsConstructor
public class PullRequestController {
    private final PullRequestService pullRequestService;

    @PostMapping
    public PullRequestResponse create(@Valid @RequestBody CreatePullRequestRequest request){
        return pullRequestService.createPullRequest(request);
    }

    @PostMapping("/{id}/reassign")
    public PullRequestResponse reassign(
            @PathVariable UUID id,
            @RequestParam("oldReviewerId") UUID oldReviewerId

    ){
        return pullRequestService.reassignReviewer(id, oldReviewerId);
    }

    @PostMapping("/{id}/merge")
    public PullRequestResponse merge(@PathVariable UUID id) {
        return pullRequestService.mergePullRequest(id);
    }

    @GetMapping("/reviewer/{userId}")
    public java.util.List<PullRequestResponse> findForReviewer(@PathVariable UUID userId) {
        return pullRequestService.getPullRequestsForReviewer(userId);
    }

    @GetMapping("/{id}")
    public PullRequestResponse getById(@PathVariable UUID id) {
        return pullRequestService.getById(id);
    }

}
