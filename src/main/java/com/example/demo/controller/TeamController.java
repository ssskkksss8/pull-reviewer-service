package com.example.demo.controller;

import com.example.demo.model.dto.TeamRequest;
import com.example.demo.model.dto.TeamResponse;
import com.example.demo.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public TeamResponse create(@Valid @RequestBody TeamRequest request) {
        return teamService.createTeam(request);
    }

    @GetMapping
    public List<TeamResponse> getAll() {
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    public TeamResponse getById(@PathVariable UUID id) {
        return teamService.getById(id);
    }

    @PutMapping("/{id}")
    public TeamResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody TeamRequest request
    ) {
        return teamService.updateTeam(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        teamService.deleteTeam(id);
    }
}
