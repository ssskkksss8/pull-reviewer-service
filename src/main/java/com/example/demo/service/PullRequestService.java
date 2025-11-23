package com.example.demo.service;

import com.example.demo.model.dto.CreatePullRequestRequest;
import com.example.demo.model.dto.PullRequestResponse;
import com.example.demo.model.entity.PullRequest;
import com.example.demo.model.entity.ReviewerAssignment;
import com.example.demo.model.entity.User;
import com.example.demo.repository.PullRequestRepository;
import com.example.demo.repository.ReviewerAssignmentRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PullRequestService {
    private final PullRequestRepository pullRequestRepository;
    private final UserRepository userRepository;
    private final ReviewerAssignmentRepository reviewerAssignmentRepository;

    private final Random random = new Random();

    @Transactional
    public PullRequestResponse createPullRequest(CreatePullRequestRequest request) {
        User author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("Author not found:" + request.getAuthorId()));

        PullRequest pr = PullRequest.builder()
                .id(UUID.randomUUID())
                .title(request.getTitle())
                .author(author)
                .status(PullRequest.Status.OPEN)
                .createdAt(Instant.now())
                .build();

        pullRequestRepository.save(pr);

        UUID teamId = author.getTeam().getId();

        List<User> candidates = userRepository.findByTeam_Id(teamId).stream()
                .filter(User::isActive)
                .filter(u -> !u.getId().equals(author.getId()))
                .toList();

        List<User> selectedReviewers = pickRandomReviewers(candidates, 2);

        for (User reviewer : selectedReviewers) {
            ReviewerAssignment assignment = ReviewerAssignment.builder()
                    .id(UUID.randomUUID())
                    .pullRequest(pr)
                    .reviewer(reviewer)
                    .build();
            reviewerAssignmentRepository.save(assignment);
        }

        return mapToResponse(pr);
    }

    @Transactional
    public PullRequestResponse mergePullRequest(UUID prId) {
        PullRequest pr = pullRequestRepository.findById(prId)
                .orElseThrow(() -> new IllegalArgumentException("PR not found: " + prId));

        if (pr.getStatus() == PullRequest.Status.MERGED) {
            return mapToResponse(pr);
        }

        pr.setStatus(PullRequest.Status.MERGED);
        pullRequestRepository.save(pr);

        return mapToResponse(pr);
    }

    @Transactional
    public PullRequestResponse reassignReviewer(UUID prId, UUID oldReviewerId) {
        PullRequest pr = pullRequestRepository.findById(prId)
                .orElseThrow(() -> new IllegalArgumentException("PR not found: " + prId));

        if (pr.getStatus() == PullRequest.Status.MERGED) {
            throw new IllegalStateException("Cannot reassign reviewers for MERGED PR");
        }

        ReviewerAssignment existing = reviewerAssignmentRepository
                .findByPullRequest_IdAndReviewer_Id(prId, oldReviewerId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Assignment not found for given PR and reviewer"
                ));

        User oldReviewer = existing.getReviewer();
        UUID teamId = oldReviewer.getTeam().getId();

        Set<UUID> alreadyAssignedIds = reviewerAssignmentRepository.findByPullRequest_Id(prId).stream()
                .map(a -> a.getReviewer().getId())
                .collect(Collectors.toSet());

        UUID authorId = pr.getAuthor().getId();

        List<User> candidates = userRepository.findByTeam_Id(teamId).stream()
                .filter(User::isActive)
                .filter(u -> !u.getId().equals(oldReviewerId))
                .filter(u -> !u.getId().equals(authorId))
                .filter(u -> !alreadyAssignedIds.contains(u.getId()))
                .toList();


        if (candidates.isEmpty()) {
            throw new IllegalStateException("No available candidates in reviewer's team for reassignment");
        }

        User newReviewer = candidates.get(random.nextInt(candidates.size()));
        existing.setReviewer(newReviewer);
        reviewerAssignmentRepository.save(existing);

        return mapToResponse(pr);
    }


    @Transactional
    public List<PullRequestResponse> getPullRequestsForReviewer(UUID reviewerId) {
        userRepository.findById(reviewerId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + reviewerId));

        List<ReviewerAssignment> assignments =
                reviewerAssignmentRepository.findByReviewer_Id(reviewerId);

        Map<UUID, PullRequest> prById = new LinkedHashMap<>();
        for (ReviewerAssignment a : assignments) {
            PullRequest pr = a.getPullRequest();
            prById.putIfAbsent(pr.getId(), pr);
        }

        return prById.values().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PullRequestResponse getById(UUID id) {
        PullRequest pr = pullRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PR not found: " + id));

        return mapToResponse(pr);
    }


    private List<User> pickRandomReviewers(List<User> candidates, int maxCount){
        if (candidates.isEmpty() || maxCount <= 0){
            return List.of();
        }
        if (candidates.size() <= maxCount){
            return List.copyOf(candidates);
        }

        List<User> shuffled = new ArrayList<>(candidates);
        Collections.shuffle(shuffled, random);
        return shuffled.subList(0, maxCount);
    }

    private PullRequestResponse mapToResponse(PullRequest pr) {
        List<UUID> reviewerIds = reviewerAssignmentRepository.findByPullRequest_Id(pr.getId()).stream()
                .map(a -> a.getReviewer().getId())
                .collect(Collectors.toList());

        return PullRequestResponse.builder()
                .id(pr.getId())
                .title(pr.getTitle())
                .authorId(pr.getAuthor().getId())
                .status(pr.getStatus())
                .createdAt(pr.getCreatedAt())
                .reviewerIds(reviewerIds)
                .build();
    }

}
