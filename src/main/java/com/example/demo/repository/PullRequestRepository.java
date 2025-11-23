package com.example.demo.repository;

import com.example.demo.model.entity.PullRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PullRequestRepository extends JpaRepository<PullRequest, String> {
}
