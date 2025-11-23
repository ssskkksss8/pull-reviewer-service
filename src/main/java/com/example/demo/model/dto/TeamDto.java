package com.example.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class TeamDto {
    @JsonProperty("team_name")
    @NotBlank
    private String teamName;

    @JsonProperty("members")
    @NotEmpty
    private List<@Valid TeamMemberDto> members;
}
