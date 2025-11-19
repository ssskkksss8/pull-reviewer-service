package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name="users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder

public class User {
    @Id
    @GeneratedValue
    private UUID id;


    @Column(nullable = false)
    private String name;

    @Column(name= "is_active", nullable = false)
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id", nullable = false)
    private Team team;


}
