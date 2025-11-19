package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "pull_request")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PullRequest {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public enum Status {
        OPEN,
        MERGED
    }

}
