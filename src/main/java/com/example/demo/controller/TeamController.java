package com.example.demo.controller;

import com.example.demo.model.dto.TeamDto;
import com.example.demo.model.dto.TeamMemberDto;
import com.example.demo.model.entity.Team;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TeamController {

    private final AssignmentService service;
    private final UserRepository userRepository;

    @PostMapping("/team/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> addTeam(@Valid @RequestBody TeamDto dto) {
        List<User> members = dto.getMembers().stream().map(this::toUser).toList();
        Team team = service.addTeam(dto.getTeamName(), members);
        List<Map<String, Object>> memberViews = userRepository.findByTeam_TeamName(team.getTeamName())
                .stream().map(this::toMemberView).toList();

        return Map.of("team", Map.of(
                "team_name", team.getTeamName(),
                "members", memberViews
        ));
    }

    @GetMapping("/team/get")
    public Map<String, Object> getTeam(@RequestParam("team_name") String teamName) {
        Team team = service.getTeam(teamName);
        List<Map<String, Object>> memberViews = userRepository.findByTeam_TeamName(teamName)
                .stream().map(this::toMemberView).toList();

        return Map.of(
                "team_name", team.getTeamName(),
                "members", memberViews
        );
    }

    private User toUser(TeamMemberDto m) {
        return User.builder()
                .userId(m.getUserId())
                .username(m.getUsername())
                .isActive(m.isActive())
                .build();
    }

    private Map<String, Object> toMemberView(User u) {
        return Map.of(
                "user_id", u.getUserId(),
                "username", u.getUsername(),
                "is_active", u.isActive()
        );
    }
}
