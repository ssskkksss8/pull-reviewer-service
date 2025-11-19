package com.example.demo.repository;

import com.example.demo.model.entity.PullRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PullRequestRepository extends JpaRepository<PullRequest, UUID> {
    List<PullRequest> findByAuthor_Id(UUID teamId);
}
