package com.example.demo.repository;


import com.example.demo.model.dto.PullRequestStatsResponse;
import com.example.demo.model.dto.ReviewerStatsResponse;
import com.example.demo.model.entity.ReviewerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface ReviewerAssignmentRepository extends JpaRepository<ReviewerAssignment, UUID>{
    List<ReviewerAssignment> findByReviewer_Id(UUID reviewerId);

    List<ReviewerAssignment> findByPullRequest_Id(UUID pullRequestId);

    boolean existsByPullRequest_IdAndReviewer_Id(UUID pullRequestId, UUID reviewerId);

    Optional<ReviewerAssignment> findByPullRequest_IdAndReviewer_Id(UUID pullRequestId, UUID reviewerId);
    @Query("""
           select new com.example.demo.model.dto.ReviewerStatsResponse(a.reviewer.id, count(a))
           from ReviewerAssignment a
           group by a.reviewer.id
           """)
    List<ReviewerStatsResponse> countAssignmentsByReviewer();

    @Query("""
           select new com.example.demo.model.dto.PullRequestStatsResponse(a.pullRequest.id, count(a))
           from ReviewerAssignment a
           group by a.pullRequest.id
           """)
    List<PullRequestStatsResponse> countReviewersByPullRequest();
}
