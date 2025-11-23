package com.example.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePrRequest {
    @JsonProperty("pull_request_id")
    @NotBlank
    private String pullRequestId;

    @JsonProperty("pull_request_name")
    @NotBlank
    private String pullRequestName;

    @JsonProperty("author_id")
    @NotBlank
    private String authorId;
}
