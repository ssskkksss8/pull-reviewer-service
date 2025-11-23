package com.example.demo.service;

import com.example.demo.model.dto.TeamMemberDto;
import com.example.demo.model.dto.TeamRequest;
import com.example.demo.model.dto.TeamResponse;
import com.example.demo.model.entity.Team;
import com.example.demo.model.entity.User;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Transactional
    public TeamResponse createTeam(TeamRequest request) {
        if (teamRepository.existsByName(request.getTeamName())) {
            throw new IllegalStateException("Team already exists: " + request.getTeamName());
        }

        Team team = Team.builder()
                .id(UUID.randomUUID())
                .name(request.getTeamName())
                .build();

        teamRepository.save(team);

        if (request.getMembers() != null) {
            for (TeamMemberDto member : request.getMembers()) {
                User user = userRepository.findByExternalId(member.getUserId())
                        .orElseGet(() -> User.builder()
                                .id(UUID.randomUUID())
                                .externalId(member.getUserId())
                                .build());

                user.setName(member.getUsername());
                user.setActive(member.isActive());
                user.setTeam(team);

                userRepository.save(user);
            }
        }

        List<User> members = userRepository.findByTeam_Id(team.getId());
        return mapToResponse(team, members);
    }

    @Transactional(readOnly = true)
    public TeamResponse getByName(String teamName) {
        Team team = teamRepository.findByName(teamName)
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + teamName));
        List<User> members = userRepository.findByTeam_Id(team.getId());
        return mapToResponse(team, members);
    }

    private TeamResponse mapToResponse(Team team, List<User> members) {
        List<TeamMemberDto> dtos = members.stream()
                .map(u -> TeamMemberDto.builder()
                        .userId(u.getExternalId())
                        .username(u.getName())
                        .active(u.isActive())
                        .build())
                .toList();

        return TeamResponse.builder()
                .teamName(team.getName())
                .members(dtos)
                .build();
    }
}
