package com.example.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SetIsActiveRequest {
    @JsonProperty("user_id")
    @NotBlank
    private String userId;

    @JsonProperty("is_active")
    private boolean isActive;
}
