package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name="reviewer_assignment",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"pr_id", "reviewer_id"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewerAssignment {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pr_id", nullable = false)
    private PullRequest pullRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

}
