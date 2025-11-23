package com.example.demo.service;

import com.example.demo.model.entity.PullRequest;
import com.example.demo.model.entity.Team;
import com.example.demo.model.entity.User;
import com.example.demo.repository.PullRequestRepository;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final PullRequestRepository pullRequestRepository;

    private final SecureRandom rng = new SecureRandom();

    @Transactional
    public Team addTeam(String teamName, List<User> rawMembers) {
        if (teamRepository.existsById(teamName)) {
            throw new DomainException("TEAM_EXISTS", "team_name already exists");
        }
        Team team = teamRepository.save(Team.builder().teamName(teamName).build());

        for (User raw : rawMembers) {
            User u = userRepository.findById(raw.getUserId()).orElse(null);
            if (u == null) {
                u = new User();
                u.setUserId(raw.getUserId());
            }
            u.setUsername(raw.getUsername());
            u.setTeam(team);
            u.setActive(raw.isActive());
            userRepository.save(u);
        }
        return team;
    }

    @Transactional(readOnly = true)
    public Team getTeam(String teamName) {
        return teamRepository.findById(teamName)
                .orElseThrow(() -> DomainException.notFound("resource not found"));
    }

    @Transactional
    public User setIsActive(String userId, boolean isActive) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> DomainException.notFound("resource not found"));
        u.setActive(isActive);
        return userRepository.save(u);
    }

    @Transactional
    public PullRequest createPr(String prId, String prName, String authorId) {
        if (pullRequestRepository.existsById(prId)) {
            throw new DomainException("PR_EXISTS", "PR id already exists");
        }

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> DomainException.notFound("resource not found"));

        String teamName = author.getTeam().getTeamName();

        List<User> candidates = userRepository.findByTeam_TeamNameAndIsActiveTrue(teamName)
                .stream()
                .filter(u -> !u.getUserId().equals(authorId))
                .collect(Collectors.toList());

        Collections.shuffle(candidates, rng);
        List<User> picked = candidates.stream().limit(2).toList();

        PullRequest pr = PullRequest.builder()
                .pullRequestId(prId)
                .pullRequestName(prName)
                .author(author)
                .status(PullRequest.Status.OPEN)
                .createdAt(OffsetDateTime.now())
                .assignedReviewers(new HashSet<>(picked))
                .build();

        return pullRequestRepository.save(pr);
    }

    @Transactional
    public PullRequest mergePr(String prId) {
        PullRequest pr = pullRequestRepository.findById(prId)
                .orElseThrow(() -> DomainException.notFound("resource not found"));

        if (pr.getStatus() == PullRequest.Status.MERGED) {
            return pr;
        }

        pr.setStatus(PullRequest.Status.MERGED);
        pr.setMergedAt(OffsetDateTime.now());
        return pullRequestRepository.save(pr);
    }

    @Transactional
    public Map.Entry<PullRequest, String> reassignReviewer(String prId, String oldUserId) {
        PullRequest pr = pullRequestRepository.findById(prId)
                .orElseThrow(() -> DomainException.notFound("resource not found"));

        if (pr.getStatus() == PullRequest.Status.MERGED) {
            throw new DomainException("PR_MERGED", "cannot reassign on merged PR");
        }

        User oldReviewer = userRepository.findById(oldUserId)
                .orElseThrow(() -> DomainException.notFound("resource not found"));

        Set<String> assignedIds = pr.getAssignedReviewers().stream()
                .map(User::getUserId).collect(Collectors.toSet());

        if (!assignedIds.contains(oldUserId)) {
            throw new DomainException("NOT_ASSIGNED", "reviewer is not assigned to this PR");
        }

        String teamName = oldReviewer.getTeam().getTeamName();

        Set<String> exclude = new HashSet<>(assignedIds);
        exclude.add(pr.getAuthor().getUserId());
        exclude.remove(oldUserId);

        List<User> candidates = userRepository.findByTeam_TeamNameAndIsActiveTrue(teamName)
                .stream()
                .filter(u -> !exclude.contains(u.getUserId()))
                .toList();

        if (candidates.isEmpty()) {
            throw new DomainException("NO_CANDIDATE", "no active replacement candidate in team");
        }

        User newReviewer = candidates.get(rng.nextInt(candidates.size()));

        pr.getAssignedReviewers().removeIf(u -> u.getUserId().equals(oldUserId));
        pr.getAssignedReviewers().add(newReviewer);

        PullRequest saved = pullRequestRepository.save(pr);
        return Map.entry(saved, newReviewer.getUserId());
    }

    @Transactional(readOnly = true)
    public List<PullRequest> getPrsForReviewer(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> DomainException.notFound("resource not found"));

        return pullRequestRepository.findAll().stream()
                .filter(pr -> pr.getAssignedReviewers().stream()
                        .anyMatch(u -> u.getUserId().equals(userId)))
                .toList();
    }
}
