package com.example.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReassignRequest {
    @JsonProperty("pull_request_id")
    @NotBlank
    private String pullRequestId;

    @JsonProperty("old_user_id")
    @NotBlank
    private String oldUserId;
}
