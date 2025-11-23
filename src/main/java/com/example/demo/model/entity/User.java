package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String userId;

    @Column(nullable = false)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_name", nullable = false)
    private Team team;

    @Column(nullable = false)
    private boolean isActive;
}
