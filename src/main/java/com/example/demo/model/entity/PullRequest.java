package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pull_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PullRequest {
    @Id
    private String pullRequestId;

    @Column(nullable = false)
    private String pullRequestName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    private OffsetDateTime mergedAt;

    @ManyToMany
    @JoinTable(
            name = "pr_reviewers",
            joinColumns = @JoinColumn(name = "pull_request_id"),
            inverseJoinColumns = @JoinColumn(name = "reviewer_id")
    )
    @Builder.Default
    private Set<User> assignedReviewers = new HashSet<>();

    public enum Status { OPEN, MERGED }
}
