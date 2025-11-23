package com.example.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeamMemberDto {
    @JsonProperty("user_id")
    @NotBlank
    private String userId;

    @JsonProperty("username")
    @NotBlank
    private String username;

    @JsonProperty("is_active")
    private boolean isActive;
}
