package com.example.demo.controller;

import com.example.demo.model.dto.SetIsActiveRequest;
import com.example.demo.model.entity.PullRequest;
import com.example.demo.model.entity.User;
import com.example.demo.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final AssignmentService service;

    @PostMapping("/users/setIsActive")
    public Map<String, Object> setIsActive(@Valid @RequestBody SetIsActiveRequest req) {
        User u = service.setIsActive(req.getUserId(), req.isActive());
        return Map.of("user", Map.of(
                "user_id", u.getUserId(),
                "username", u.getUsername(),
                "team_name", u.getTeam().getTeamName(),
                "is_active", u.isActive()
        ));
    }

    @GetMapping("/users/getReview")
    public Map<String, Object> getReview(@RequestParam("user_id") String userId) {
        List<PullRequest> prs = service.getPrsForReviewer(userId);

        List<Map<String, Object>> shortPrs = prs.stream()
                .map(pr -> Map.of(
                        "pull_request_id", pr.getPullRequestId(),
                        "pull_request_name", pr.getPullRequestName(),
                        "author_id", pr.getAuthor().getUserId(),
                        "status", pr.getStatus().name()
                ))
                .toList();

        return Map.of(
                "user_id", userId,
                "pull_requests", shortPrs
        );
    }
}
