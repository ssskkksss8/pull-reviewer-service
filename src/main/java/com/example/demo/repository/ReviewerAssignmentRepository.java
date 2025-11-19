package com.example.demo.repository;


import com.example.demo.model.entity.ReviewerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewerAssignmentRepository extends JpaRepository<ReviewerAssignment, UUID>{
    List<ReviewerAssignment> findByReviewer_Id(UUID reviewerId);

    List<ReviewerAssignment> findByPullRequest_Id(UUID pullRequestId);

    boolean existsByPullRequest_IdAndReviewer_Id(UUID pullRequestId, UUID reviewerId);

}
