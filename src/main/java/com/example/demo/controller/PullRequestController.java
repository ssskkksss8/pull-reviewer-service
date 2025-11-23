package com.example.demo.controller;

import com.example.demo.model.dto.CreatePrRequest;
import com.example.demo.model.dto.MergePrRequest;
import com.example.demo.model.dto.ReassignRequest;
import com.example.demo.model.entity.PullRequest;
import com.example.demo.model.entity.User;
import com.example.demo.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PullRequestController {

    private final AssignmentService service;

    @PostMapping("/pullRequest/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@Valid @RequestBody CreatePrRequest req) {
        PullRequest pr = service.createPr(req.getPullRequestId(), req.getPullRequestName(), req.getAuthorId());
        return Map.of("pr", toDto(pr));
    }

    @PostMapping("/pullRequest/merge")
    public Map<String, Object> merge(@Valid @RequestBody MergePrRequest req) {
        PullRequest pr = service.mergePr(req.getPullRequestId());
        return Map.of("pr", toDto(pr));
    }

    @PostMapping("/pullRequest/reassign")
    public Map<String, Object> reassign(@Valid @RequestBody ReassignRequest req) {
        var res = service.reassignReviewer(req.getPullRequestId(), req.getOldUserId());
        return Map.of(
                "pr", toDto(res.getKey()),
                "replaced_by", res.getValue()
        );
    }

    private Map<String, Object> toDto(PullRequest pr) {
        return Map.of(
                "pull_request_id", pr.getPullRequestId(),
                "pull_request_name", pr.getPullRequestName(),
                "author_id", pr.getAuthor().getUserId(),
                "status", pr.getStatus().name(),
                "assigned_reviewers", pr.getAssignedReviewers().stream().map(User::getUserId).toList(),
                "createdAt", pr.getCreatedAt(),
                "mergedAt", pr.getMergedAt()
        );
    }
}
