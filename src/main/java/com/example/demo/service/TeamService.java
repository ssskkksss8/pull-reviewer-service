package com.example.demo.service;

import com.example.demo.model.dto.TeamRequest;
import com.example.demo.model.dto.TeamResponse;
import com.example.demo.model.entity.Team;
import com.example.demo.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamResponse createTeam(TeamRequest request) {
        Team team = Team.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .build();

        teamRepository.save(team);
        return map(team);
    }

    public List<TeamResponse> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(this::map)
                .toList();
    }

    public TeamResponse getById(UUID id) {
        return teamRepository.findById(id)
                .map(this::map)
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + id));
    }

    public TeamResponse updateTeam(UUID id, TeamRequest request) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + id));

        team.setName(request.getName());
        teamRepository.save(team);

        return map(team);
    }

    public void deleteTeam(UUID id) {
        if (!teamRepository.existsById(id)) {
            throw new IllegalArgumentException("Team not found: " + id);
        }
        teamRepository.deleteById(id);
    }

    private TeamResponse map(Team team) {
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .build();
    }
}
