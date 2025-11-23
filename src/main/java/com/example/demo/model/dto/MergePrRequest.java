package com.example.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MergePrRequest {
    @JsonProperty("pull_request_id")
    @NotBlank
    private String pullRequestId;
}
