package com.example.demo.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeamRequest {

    @NotBlank
    private String Name;
}
